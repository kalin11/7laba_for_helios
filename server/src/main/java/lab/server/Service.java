package lab.server;

import cmd.Cmd;
import collection.LinkedCollection;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import command.CommandName;
import command.parsing.Command;
import command.parsing.CommandLineParser;
import command.tasksCommands.with_arguments.*;
import command.tasksCommands.without_arguments.*;
import entity.Movie;
import visitor.Visitor;
import visitor.VisitorImpl;

import java.io.IOError;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.util.HashMap;
import java.util.HashSet;

public class Service /*implements Runnable*/ {
    private Object o;
    private LinkedCollection collection;
    private Cmd cmd;
    private VisitorImpl visitor = new VisitorImpl(collection, initCommands());
    private Repository repository;
    private final HashSet<String> DBcmds = new HashSet<>();

    public Service(LinkedCollection linkedCollection, Cmd cmd) {
        this.collection = linkedCollection;
        this.cmd = cmd;
        DBcmds.add("add");
        DBcmds.add("update");
        DBcmds.add("remove_by_id");
        DBcmds.add("remove_lower");
        DBcmds.add("remove_greater");
        DBcmds.add("clear");
        DBcmds.add("execute_script");

    }
    //вот здесь будет логика выполнения команды, передается сама команда и коллекция с которой нужно работать
    //все это


//    ride
//    public void run() {
//        /*вот здесь идет свитч кейс по командам тот и происходит само исполнение, непосредственно
//        если команда из списка, которые требуют БД, то идем в репозиторий*/
//        try {
//            if (cmd.getArguments() == null) {
//                //todo возврат говна
//                //add, remove_lower, remove_greater
//                if (cmd.getName().equals("add") || cmd.getName().equals("remove_lower") || cmd.getName().equals("remove_greater")) {
//                    Movie movie = cmd.getMovie();
//                    String[] temp = cmd.toString().split(" ");
//                    if (temp[2] != null) {
//                        CommandLineParser parser = new CommandLineParser(temp[0] + " " + movie.allInfo(), initCommands());
//                        repository = new Repository(cmd, movie.allInfo());
//                        o = parser.exe(visitor);
//                    }
//                } else {
//                    if (cmd.getName().equals("clear")){
//                        repository = new Repository(cmd);
//                        CommandLineParser parser = new CommandLineParser(cmd.getName(), initCommands());
//
//                    }
//                    else {
//                        //help, info и тд
//                        CommandLineParser parser = new CommandLineParser(cmd.getName(), initCommands());
//                        o = parser.exe(visitor);
//                    }
//                }
//            } else {
//                if (cmd.getMovie() != null) {
//                    //update
//                    CommandLineParser parser = new CommandLineParser(cmd.getName() + " " + cmd.getArguments() + " " + cmd.getMovie().allInfo(), initCommands());
//                    o = parser.exe(visitor);
//                } else {
//                    //script
//                    CommandLineParser parser = new CommandLineParser(cmd.getName() + " " + cmd.getArguments() + " " + cmd.getFile(), initCommands());
//                    o = parser.exe(visitor);
//                }
//            }
//        }catch (IOException e){
//            this.exception = e;
//        }
//    }
//
//    public synchronized IOException getException(){
//        if (exception == null){
//            return null;
//        }
//        else {
//            return exception;
//        }
//    }
    public Object doCommand() throws IOException{
            if (cmd.getArguments() == null) {
                //todo возврат говна
                //add, remove_lower, remove_greater
                if (cmd.getName().equals("add") || cmd.getName().equals("remove_lower") || cmd.getName().equals("remove_greater")) {
                    Movie movie = cmd.getMovie();
                    String[] temp = cmd.toString().split(" ");
                    if (temp[2] != null) {
                        CommandLineParser parser = new CommandLineParser(temp[0] + " " + movie.allInfo(), initCommands());
//                        repository = new Repository(cmd, movie.allInfo());
                        return o = parser.exe(visitor);
                    }
                } else {
                    if (cmd.getName().equals("clear")){
//                        repository = new Repository(cmd);
                        CommandLineParser parser = new CommandLineParser(cmd.getName(), initCommands());
                        return o = parser.exe(visitor);

                    }
                    else {
                        //help, info и тд
                        CommandLineParser parser = new CommandLineParser(cmd.getName(), initCommands());
                        return o = parser.exe(visitor);
                    }
                }
            } else {
                if (cmd.getMovie() != null) {
                    //update
                    CommandLineParser parser = new CommandLineParser(cmd.getName() + " " + cmd.getArguments() + " " + cmd.getMovie().allInfo(), initCommands());
                    return o = parser.exe(visitor);
                } else {
                    //script
                    CommandLineParser parser = new CommandLineParser(cmd.getName() + " " + cmd.getArguments() + " " + cmd.getFile(), initCommands());
                    return o = parser.exe(visitor);
                }
            }
            return "nothing";
        }

    public static HashMap<CommandName, Command> initCommands() {
        HashMap<CommandName, Command> commands = new HashMap<>();
        commands.put(CommandName.HELP, new HelpCommand());
        commands.put(CommandName.EXIT, new ExitCommand());
        commands.put(CommandName.ADD, new AddCommand());
        commands.put(CommandName.SHOW, new ShowCommand());
        commands.put(CommandName.HEAD, new HeadCommand());
        commands.put(CommandName.INFO, new InfoCommand());
        commands.put(CommandName.AVERAGE_OF_LENGTH, new AverageOfLength());
        commands.put(CommandName.PRINT_UNIQUE_OSCARS_COUNT, new PrintUniqueOscarsCount());
        commands.put(CommandName.UPDATE_ID, new UpdateCommand());
        commands.put(CommandName.REMOVE_BY_ID, new RemoveCommand());
        commands.put(CommandName.COUNT_BY_GENRE, new CountByGenreCommand());
        commands.put(CommandName.REMOVE_GREATER, new RemoveGreaterCommand());
        commands.put(CommandName.CLEAR, new ClearCommand());
        commands.put(CommandName.REMOVE_LOWER, new RemoveLowerCommand());
        commands.put(CommandName.EXECUTE_SCRIPT, new ExecuteScriptCommand());
        return commands;
    }
}
