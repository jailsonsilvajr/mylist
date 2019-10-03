package com.jailson.mylist.service;

import android.widget.ProgressBar;
import com.jailson.mylist.http.AddItem;
import com.jailson.mylist.http.AddList;
import com.jailson.mylist.http.DeleteItem;
import com.jailson.mylist.http.DeleteList;
import com.jailson.mylist.http.GetItems;
import com.jailson.mylist.http.GetLists;
import com.jailson.mylist.http.Login;
import com.jailson.mylist.http.RegisterUser;
import com.jailson.mylist.http.UpdateItem;
import com.jailson.mylist.object.Item;
import com.jailson.mylist.object.User;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class Service {

    private final String url = "https://jjsj2.000webhostapp.com/mylist/";

    public User login(String email, String password) throws ExecutionException, InterruptedException {

        Login login = new Login(this.url + "user/login.php", email, password);
        return login.execute().get();
    }

    public User register_user(String name, String email, String password) throws ExecutionException, InterruptedException {

        RegisterUser registerUser = new RegisterUser(this.url + "user/register.php", name, email, password);
        return registerUser.execute().get();
    }

    public List<com.jailson.mylist.object.List> getLists(int id_user) throws ExecutionException, InterruptedException {

        GetLists getLists = new GetLists(this.url + "list/get_list.php", id_user);
        return getLists.execute().get();
    }

    public boolean deleteList(int id) throws ExecutionException, InterruptedException {

        DeleteList deleteList = new DeleteList(this.url + "list/delete_list.php", id);
        return deleteList.execute().get();
    }

    public boolean addList(String name, int id_user) throws ExecutionException, InterruptedException {

        AddList addList = new AddList(this.url + "list/add_list.php", name, id_user);
        return addList.execute().get();
    }

    public List<Item> getItens(int id_list) throws ExecutionException, InterruptedException {

        GetItems getItems = new GetItems(this.url + "item/get_item.php", id_list);
        return getItems.execute().get();
    }

    public boolean addItem(Item item) throws ExecutionException, InterruptedException {

        AddItem add = new AddItem(this.url + "item/add_item.php", item);
        return add.execute().get();
    }

    public boolean deleteItem(Item item) throws ExecutionException, InterruptedException {

        DeleteItem deleteItem = new DeleteItem(this.url + "item/delete_item.php", item);
        return deleteItem.execute().get();
    }

    public boolean updateItem(Item item) throws ExecutionException, InterruptedException {

        UpdateItem updateItem = new UpdateItem(this.url + "item/update_item.php", item);
        return updateItem.execute().get();
    }
}
