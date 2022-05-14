package command.tasksCommands.without_arguments;

import cmd.Cmd;
import command.parsing.CommandWithoutMovie;
import visitor.Visitor;

import java.io.BufferedReader;

public class ShowCommand implements CommandWithoutMovie {
    private String[] args;

    public String[] getArgs(){
        return args;
    }


    public String toString(){
        return "show - вывести в стандартный поток вывода все элементы коллекции в строковом представлении";
    }


    public Object accept(Visitor v) {
        return v.visit(this);
    }

    @Override
    public Object accept(Visitor v, Cmd cmd) {
        return v.visit(this, cmd);
    }


    public boolean execute(String[] arguments, BufferedReader in) {
        args = arguments;
        return true;
    }


    public boolean valid(String[] args) {
        this.args = args;
        if (args.length!=0){
            System.out.println("команда 'show' должна быть без аргументов");
            return false;
        }
        else {
            return true;
        }
    }
}
