package com.jailson.mylist.service;

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

public class Service {

    private final String url = "https://jjsj2.000webhostapp.com/mylist/";

    public User login(String email, String password) {

        Login login = new Login(this.url + "user/login.php", email, password);
        return login.do_login();
    }

    public User register_user(String name, String email, String password) {

        RegisterUser registerUser = new RegisterUser(this.url + "user/register.php", name, email, password);
        return registerUser.do_register();
    }

    public List<com.jailson.mylist.object.List> getLists(int id_user) {

        GetLists getLists = new GetLists(this.url + "list/get_list.php", id_user);
        return getLists.do_getList();
    }

    public boolean deleteList(int id) {

        DeleteList deleteList = new DeleteList(this.url + "list/delete_list.php", id);
        return deleteList.do_delete();
    }

    public boolean addList(String name, int id_user) {

        AddList addList = new AddList(this.url + "list/add_list.php", name, id_user);
        return addList.do_addList();
    }

    public List<Item> getItens(int id_list) {

        GetItems getItems = new GetItems(this.url + "item/get_item.php", id_list);
        return getItems.do_get();
    }

    public boolean addItem(Item item) {

        AddItem add = new AddItem(this.url + "item/add_item.php", item);
        return add.do_add();
    }

    public boolean deleteItem(Item item) {

        DeleteItem deleteItem = new DeleteItem(this.url + "item/delete_item.php", item);
        return deleteItem.do_delete();
    }

    public boolean updateItem(Item item) {

        UpdateItem updateItem = new UpdateItem(this.url + "item/update_item.php", item);
        return updateItem.do_update();
    }
}
