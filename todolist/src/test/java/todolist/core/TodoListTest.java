package todolist.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertSame;
import java.util.Collection;
import java.util.Iterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TodoListTest {

  private TodoList newList;

  @BeforeEach
  public void setUp() {
    newList = new TodoList();
  }

  // tests for getCheckedItems

  @Test
  public void testGetCheckedTodoItems_emptyList() {
    assertTrue(newList.getCheckedTodoItems().isEmpty());
  }

  @Test
  public void testGetCheckedTodoItems_oneUncheckedItem() {
    TodoItem item = newList.createTodoItem();
    newList.addTodoItem(item);
    assertTrue(newList.getCheckedTodoItems().isEmpty());
  }

  @Test
  public void testGetCheckedTodoItems_oneCheckedItem() {
    TodoItem item = newList.createTodoItem();
    newList.addTodoItem(item);
    item.setChecked(true);
    Collection<TodoItem> items = newList.getCheckedTodoItems();
    assertEquals(1, items.size());
    assertSame(item, items.iterator().next());
  }

  @Test
  public void testGetCheckedTodoItems_oneItemCheckedAfterAdding() {
    TodoItem item = newList.createTodoItem();
    newList.addTodoItem(item);
    assertTrue(newList.getCheckedTodoItems().isEmpty());
    item.setChecked(true);
    Collection<TodoItem> items = newList.getCheckedTodoItems();
    assertEquals(1, items.size());
    assertSame(item, items.iterator().next());
  }

  // tests for getUncheckedTodoItems

  @Test
  public void testGetUncheckedTodoItems_emptyList() {
    assertTrue(newList.getUncheckedTodoItems().isEmpty());
  }

  @Test
  public void testGetUncheckedTodoItems_oneUncheckedItem() {
    TodoItem item = newList.createTodoItem();
    newList.addTodoItem(item);
    Collection<TodoItem> items = newList.getUncheckedTodoItems();
    assertEquals(1, items.size());
    assertSame(item, items.iterator().next());
  }

  @Test
  public void testGetUncheckedTodoItems_oneCheckedItem() {
    TodoItem item = newList.createTodoItem();
    newList.addTodoItem(item);
    item.setChecked(true);
    assertTrue(newList.getUncheckedTodoItems().isEmpty());
  }

  @Test
  public void testGetUncheckedTodoItems_oneItemCheckedAfterAdding() {
    TodoItem item = newList.createTodoItem();
    newList.addTodoItem(item);
    Collection<TodoItem> items = newList.getUncheckedTodoItems();
    assertEquals(1, items.size());
    assertEquals(item, items.iterator().next());
    item.setChecked(true);
    assertTrue(newList.getUncheckedTodoItems().isEmpty());
  }

  // tests for getTodoItems

  @Test
  public void testGetTodoItems_emptyList() {
    assertTrue(newList.getTodoItems().isEmpty());
  }

  @Test
  public void testGetTodoItems_oneUncheckedItem() {
    TodoItem item = newList.createTodoItem();
    newList.addTodoItem(item);
    Collection<TodoItem> items = newList.getTodoItems();
    assertEquals(1, items.size());
    assertSame(item, items.iterator().next());
  }

  @Test
  public void testGetTodoItems_oneCheckedItem() {
    TodoItem item = newList.createTodoItem();
    item.setChecked(true);
    newList.addTodoItem(item);
    Collection<TodoItem> items = newList.getTodoItems();
    assertEquals(1, items.size());
    assertSame(item, items.iterator().next());
  }

  @Test
  public void testGetTodoItems_oneItemCheckedAfterAdding() {
    TodoItem item = newList.createTodoItem();
    newList.addTodoItem(item);
    Collection<TodoItem> items1 = newList.getTodoItems();
    assertEquals(1, items1.size());
    assertEquals(item, items1.iterator().next());
    item.setChecked(true);
    Collection<TodoItem> items2 = newList.getTodoItems();
    assertEquals(1, items2.size());
    assertSame(item, items2.iterator().next());
  }

  // test iterator

  private void checkIterator(Iterator<TodoItem> it, TodoItem... items) {
    int i = 0;
    while (it.hasNext()) {
      assertTrue(i < items.length);
      assertSame(items[i], it.next());
      i++;
    }
    assertTrue(i == items.length);
  }

  @Test
  public void testIterator_addingAndRemovingItems() {
    checkIterator(newList.iterator());
    TodoItem item1 = newList.createTodoItem(), item2 = newList.createTodoItem(), item3 = newList.createTodoItem();
    // sikre at de ikke er equals
    item1.setText("Item 1");
    item2.setText("Item 2");
    item3.setText("Item 3");
    newList.addTodoItem(item1);
    checkIterator(newList.iterator(), item1);
    newList.addTodoItem(item2);
    checkIterator(newList.iterator(), item1, item2);
    newList.addTodoItem(item3);
    checkIterator(newList.iterator(), item1, item2, item3);
    newList.removeTodoItem(item2);
    checkIterator(newList.iterator(), item1, item3);
    newList.removeTodoItem(item1);
    checkIterator(newList.iterator(), item3);
    newList.removeTodoItem(item3);
    checkIterator(newList.iterator());
  }

  private int receivedNotificationCount = 0;

  @Test
  public void testFireTodoListChanged_addItemAndReceiveNotification() {
    TodoListListener listener = list -> {
      receivedNotificationCount++;
    };
    newList.addTodoListListener(listener);
    assertEquals(0, receivedNotificationCount);
    TodoItem item = newList.createTodoItem();
    newList.addTodoItem(item);
    assertEquals(1, receivedNotificationCount);
    newList.removeTodoItem(item);
    assertEquals(2, receivedNotificationCount);
    item.setText("endret verdi");
    assertEquals(3, receivedNotificationCount);
    item.setChecked(true);
    assertEquals(4, receivedNotificationCount);
    newList.removeTodoListListener(listener);
    item.setChecked(false);
    assertEquals(4, receivedNotificationCount);
  }

  @Test
  public void testFireTodoListChanged_addItemAndMockReceiveNotification() {
    TodoListListener listener = mock(TodoListListener.class);
    newList.addTodoListListener(listener);
    verify(listener, times(0)).todoListChanged(newList);
    TodoItem item = newList.createTodoItem();
    newList.addTodoItem(item);
    verify(listener, times(1)).todoListChanged(newList);
    newList.removeTodoItem(item);
    verify(listener, times(2)).todoListChanged(newList);
    item.setText("endret verdi");
    verify(listener, times(3)).todoListChanged(newList);
    item.setChecked(true);
    verify(listener, times(4)).todoListChanged(newList);
  }
}
