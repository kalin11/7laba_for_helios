package lab.server;

import DB.DataBase.GetCollectionFromDB;
import collection.LinkedCollection;

import java.net.BindException;
import java.sql.SQLException;

public class Server {
    public static void main(String[] args) throws BindException, SQLException, ClassNotFoundException {
        //todo ну тут короче будет предложение для ввода хоста и порта
        //todo UnresolvedAddressExecep
        //todo BindExecep
//        String host = "se.ifmo.ru";
//        int port = 16000;
        System.out.println("Сервер запущен!");
        GetCollectionFromDB get = new GetCollectionFromDB();
        get.getCollection();
        GetCollectionFromDB getCollection = new GetCollectionFromDB();
        LinkedCollection collection = getCollection.getCollection();
        System.out.println(collection);
        Controller controller = new Controller();
        controller.acceptConnection(collection);

    }
}
