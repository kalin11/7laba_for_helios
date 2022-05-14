package DB.DataBase;

import collection.LinkedCollection;
import entity.*;
import sun.awt.image.ImageWatched;

import java.sql.*;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class GetCollectionFromDB {
    //данные БД надо с properties
//    private static final String url = "jdbc:postgresql://localhost:63333/studs";
    private static final String url = "jdbc:postgresql://pg:5432/studs";
    private static final String username = "s336805";
    private static final String password = "ipb588";
    private static final String driver = "org.postgresql.Driver";
    private LinkedCollection collection = new LinkedCollection();
    private List<Movie> list = Collections.synchronizedList(new LinkedList<>());

    public LinkedCollection getCollection() throws SQLException, ClassNotFoundException {
        Class.forName(driver);
        PreparedStatement ps;
        ResultSet resultSet;
        Connection connection = DriverManager.getConnection(url, username, password);
        String getOneRow = "select num, title, x, y, creation_date, oscars_count, length,  genre, rating, name, birthday, weight, nationality from \"Movies\" as M inner join \"Persons\" P on P.id = M.person";
        ps = connection.prepareStatement(getOneRow);
        ps.execute();
        resultSet = ps.getResultSet();
        while (resultSet.next()){
            Movie movie = new Movie(1, "name", new Coordinates(1,2), new Date(), 1, 12L, MovieGenre.ACTION, MpaaRating.G,
                    new Person("1", ZonedDateTime.now(), 1.5F, Country.INDIA));
            Person person = new Person("Vasya", ZonedDateTime.now(), 0.1F, Country.INDIA);
            movie.setId(resultSet.getInt(1));
            movie.setName(resultSet.getString(2));
            movie.setCoordinates(resultSet.getInt(3), resultSet.getInt(4));
            movie.setCreationDate(resultSet.getTimestamp(5));
            movie.setOscarsCount(resultSet.getInt(6));
            movie.setLength(resultSet.getLong(7));
            movie.setGenre(MovieGenre.values()[resultSet.getInt(8)]);
            movie.setMpaaRating(MpaaRating.values()[resultSet.getInt(9)]);
            person.setName(resultSet.getString(10));
            person.setBirthday(fromTimestamp(resultSet, 11));
//            person.setBirthday(ZonedDateTime.now());
            person.setWeight(resultSet.getFloat(12));
            person.setNationality(Country.values()[resultSet.getInt(13)]);
            movie.setOperator(person);
            collection.add(movie);
        }
        return collection;
    }

    public List<Movie> getList() throws SQLException, ClassNotFoundException{
        Class.forName(driver);
        PreparedStatement ps;
        ResultSet resultSet;
        Connection connection = DriverManager.getConnection(url, username, password);
        String getOneRow = "select num, title, x, y, creation_date, oscars_count, length,  genre, rating, name, birthday, weight, nationality from \"Movies\" as M inner join \"Persons\" P on P.id = M.person";
        ps = connection.prepareStatement(getOneRow);
        ps.execute();
        resultSet = ps.getResultSet();
        while (resultSet.next()){
            Movie movie = new Movie(1, "name", new Coordinates(1,2), new Date(), 1, 12L, MovieGenre.ACTION, MpaaRating.G,
                    new Person("1", ZonedDateTime.now(), 1.5F, Country.INDIA));
            Person person = new Person("Vasya", ZonedDateTime.now(), 0.1F, Country.INDIA);
            movie.setId(resultSet.getInt(1));
            movie.setName(resultSet.getString(2));
            movie.setCoordinates(resultSet.getInt(3), resultSet.getInt(4));
            movie.setCreationDate(resultSet.getTimestamp(5));
            movie.setOscarsCount(resultSet.getInt(6));
            movie.setLength(resultSet.getLong(7));
            movie.setGenre(MovieGenre.values()[resultSet.getInt(8)]);
            movie.setMpaaRating(MpaaRating.values()[resultSet.getInt(9)]);
            person.setName(resultSet.getString(10));
            person.setBirthday(fromTimestamp(resultSet, 11));
//            person.setBirthday(ZonedDateTime.now());
            person.setWeight(resultSet.getFloat(12));
            person.setNationality(Country.values()[resultSet.getInt(13)]);
            movie.setOperator(person);
            list.add(movie);
            collection.setList(list);
        }
        return Collections.synchronizedList(list);
    }

    public ZonedDateTime fromTimestamp(ResultSet rs, int column) throws SQLException{
        Timestamp timestamp = rs.getTimestamp(column);
        return getDateTime(timestamp);
    }

    public ZonedDateTime getDateTime(Timestamp timestamp){
        return timestamp != null ? ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestamp.getTime()), ZoneOffset.UTC) : null;
    }
}
