package DB.DataBase;

import cmd.Cmd;
import entity.Movie;


import java.sql.*;
import java.util.Date;
import java.util.LinkedList;

public class DBSender {
    public Object send(Connection connection, Cmd cmd) throws SQLException {
        try {
            PreparedStatement ps = connection.prepareStatement("");
            ResultSet resultSet = null;
            GetCollectionFromDB get = new GetCollectionFromDB();
            String getID;
            String sql;
            String delPer;
            int id = -1;
            switch (cmd.getName()) {
                case "clear":
                    //короче, тут 3 запроса, сначала получаем айдишники по владельцу, потом мы удаляем это из Персонс
                    if (checkExesting(connection, cmd)){
                        getID = "select person from \"Movies\" where owner = ?";
                        ps = connection.prepareStatement(getID);
                        ps.setString(1, cmd.getLogin());
                        ps.execute();
                        resultSet = ps.getResultSet();
                        while (resultSet.next()) {
                            id = resultSet.getInt(1);
                            System.out.println(id);
                            sql = "delete from \"Movies\" where owner = ?";
                            ps = connection.prepareStatement(sql);
                            ps.setString(1, cmd.getLogin());
                            ps.execute();
                            delPer = "delete from \"Persons\" where id = ?";
                            ps = connection.prepareStatement(delPer);
                            ps.setInt(1, id);
                            ps.execute();
                        }
                        return get.getList();
                    }
                    else return "нет объектов, принадлежащих вам";
                case "remove_by_id":
                    if (checkForRemoveById(connection, cmd)) {
                        getID = "select person from \"Movies\" where owner = ?";
                        ps = connection.prepareStatement(getID);
                        ps.setString(1, cmd.getLogin());
                        ps.execute();
                        resultSet = ps.getResultSet();
                        if (resultSet.next()) {
                            long x = resultSet.getLong(1);
                            sql = "delete from \"Movies\" where owner = ? and num = ?";
                            ps = connection.prepareStatement(sql);
                            ps.setString(1, cmd.getLogin());
                            ps.setLong(2, Long.parseLong(cmd.getArguments()));
                            ps.execute();
                            delPer = "delete from \"Persons\" where id = ?";
                            ps = connection.prepareStatement(delPer);
                            ps.setLong(1, x);
                            ps.execute();
                        }
                        return get.getList();
                    }
                    else {
                        return "нет объектов с таким id, принадлежащим вам";
                    }
                case "add":
                    String add = "insert into \"Persons\" (name, birthday, weight, nationality) VALUES (?, ?, ?, ?) returning id";
                    ps = connection.prepareStatement(add);
                    ps.setString(1,cmd.getMovie().getOperator().getPersonsName());
                    ps.setTimestamp(2, Timestamp.valueOf(cmd.getMovie().getOperator().getBirthday().toLocalDate().atStartOfDay()));
                    ps.setFloat(3, cmd.getMovie().getOperator().getWeight());
                    ps.setInt(4, cmd.getMovie().getOperator().getNationality().ordinal());
                    ps.execute();
                    resultSet = ps.getResultSet();
                    if (resultSet.next()){
                        int person = resultSet.getInt(1);
                        sql = "insert into \"Movies\" (title, x, y, creation_date, oscars_count, length, genre, rating, person, owner) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                        ps = connection.prepareStatement(sql);
                        ps.setString(1, cmd.getMovie().getMovieName());
                        ps.setInt(2, cmd.getMovie().getCoordinates().getX());
                        ps.setInt(3, cmd.getMovie().getCoordinates().getY());
                        ps.setDate(4, new java.sql.Date(cmd.getMovie().getCreationDate().getTime()));
                        ps.setInt(5, cmd.getMovie().getOscarsCount());
                        ps.setLong(6, cmd.getMovie().getLength());
                        ps.setInt(7, cmd.getMovie().getGenre().ordinal());
                        ps.setInt(8, cmd.getMovie().getGenre().ordinal());
                        ps.setInt(9, person);
                        ps.setString(10, cmd.getLogin());
                        ps.execute();
                    }
                    return get.getList();
                case "remove_lower":
                    if (checkExesting(connection, cmd)){
                        getID = "select person from \"Movies\" where owner = ?";
                        ps = connection.prepareStatement(getID);
                        ps.setString(1, cmd.getLogin());
                        ps.execute();
                        resultSet = ps.getResultSet();
                        while (resultSet.next()) {
                            id = resultSet.getInt(1);
                            long x = resultSet.getLong(1);
                            sql = "delete from \"Movies\" where owner = ? and oscars_count < ?";
                            ps = connection.prepareStatement(sql);
                            ps.setString(1, cmd.getLogin());
                            ps.setInt(2, cmd.getMovie().getOscarsCount());
                            ps.execute();
                            delPer = "delete from \"Persons\" where id = ?";
                            ps = connection.prepareStatement(delPer);
                            ps.setLong(1, x);
                            ps.execute();
                        }
                        return get.getList();
                    }
                    else return "нет объектов, принадлежащих вам";

                case "remove_greater":
                    if (checkExesting(connection, cmd)){
                        getID = "select person from \"Movies\" where owner = ?";
                        ps = connection.prepareStatement(getID);
                        ps.setString(1, cmd.getLogin());
                        ps.execute();
                        resultSet = ps.getResultSet();
                        while (resultSet.next()) {
                            id = resultSet.getInt(1);
                            long x = resultSet.getLong(1);
                            sql = "delete from \"Movies\" where owner = ? and oscars_count > ?";
                            ps = connection.prepareStatement(sql);
                            ps.setString(1, cmd.getLogin());
                            ps.setInt(2, cmd.getMovie().getOscarsCount());
                            ps.execute();
                            delPer = "delete from \"Persons\" where id = ?";
                            ps = connection.prepareStatement(delPer);
                            ps.setLong(1, x);
                            ps.execute();
                        }
                        return get.getList();
                    }
                    else return "нет объектов, принадлежащих вам";
                case "update":
                    if (checkExesting(connection, cmd)){
                        getID = "select person from \"Movies\" where owner = ?";
                        ps = connection.prepareStatement(getID);
                        ps.setString(1, cmd.getLogin());
                        ps.execute();
                        resultSet = ps.getResultSet();
                        if (resultSet.next()){
                            long x = resultSet.getLong(1);
                            ResultSet set;
                            sql = "update \"Movies\" set title = ?, x = ?, y = ?, creation_date = ?, length = ?, genre = ?, rating = ? where num = ? and owner = ? returning person";
                            ps = connection.prepareStatement(sql);
                            ps.setString(1, cmd.getMovie().getMovieName());
                            ps.setInt(2, cmd.getMovie().getCoordinates().getX());
                            ps.setInt(3, cmd.getMovie().getCoordinates().getY());
                            ps.setDate(4, new java.sql.Date(cmd.getMovie().getCreationDate().getTime()));
                            ps.setLong(5, cmd.getMovie().getLength());
                            ps.setInt(6, cmd.getMovie().getGenre().ordinal());
                            ps.setInt(7, cmd.getMovie().getMpaaRating().ordinal());
                            ps.setLong(8, Long.parseLong(cmd.getArguments()));
                            ps.setString(9, cmd.getLogin());
                            ps.execute();
                            String person = "update \"Persons\" set name = ?, birthday = ?, weight = ?, nationality = ?";
                            ps = connection.prepareStatement(person);
                            ps.setString(1, cmd.getMovie().getOperator().getPersonsName());
                            ps.setTimestamp(2, Timestamp.valueOf(cmd.getMovie().getOperator().getBirthday().toLocalDate().atStartOfDay()));
                            ps.setFloat(3, cmd.getMovie().getOperator().getWeight());
                            ps.setInt(4, cmd.getMovie().getOperator().getNationality().ordinal());
                            ps.execute();
                        }
                        return get.getList();
                    }
                    else return "нет объектов, принадлежащих вам";

            }
        }catch (ClassNotFoundException e){
            System.out.println("ClassNotFound");
        }
        return new LinkedList<Movie>();
    }

    public boolean checkExesting(Connection connection, Cmd cmd){
        try{
            int c = -1;
            String check = "select count(*) from \"Movies\" where owner = ?";
            PreparedStatement ps = connection.prepareStatement(check);
            ps.setString(1, cmd.getLogin());
            ps.execute();
            ResultSet resultSet = ps.getResultSet();
            if (resultSet.next()){
                c = resultSet.getInt(1);
            }
            return (c>0);
        }catch (SQLException e){
            return false;
        }
    }

    public boolean checkForRemoveById(Connection connection, Cmd cmd){
        try{
            int c = -1;
            String check = "select count(*) from \"Movies\" where owner = ? and num = ?";
            PreparedStatement ps = connection.prepareStatement(check);
            ps.setString(1, cmd.getLogin());
            ps.setLong(2, Long.parseLong(cmd.getArguments()));
            ps.execute();
            ResultSet resultSet = ps.getResultSet();
            if (resultSet.next()){
                c = resultSet.getInt(1);
            }
            return c > 0;
        }catch (SQLException e){
            return false;
        }

    }

}
