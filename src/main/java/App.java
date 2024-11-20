import com.example.my_own_kitchen_java_funcional.commands.CrateCommands;
import com.example.my_own_kitchen_java_funcional.commands.FoodCommands;
import com.example.my_own_kitchen_java_funcional.commands.RecipeCommands;
import org.jline.console.SystemRegistry;
import org.jline.console.impl.Builtins;
import org.jline.console.impl.SystemRegistryImpl;
import org.jline.keymap.KeyMap;
import org.jline.reader.*;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.widget.TailTipWidgets;
import picocli.CommandLine;
import picocli.shell.jline3.PicocliCommands;
import org.fusesource.jansi.AnsiConsole;

import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Supplier;

public class App {
    @CommandLine.Command(name = "",
            description = { // descripción del comando raíz
                    "Example interactive shell with completion and autosuggestions. " +
                            "Hit @|magenta <TAB>|@ to see available commands.",
                    "Hit @|magenta ALT-S|@ to toggle tailtips.",
                    ""},
            footer = {"", "Press Ctrl-D to exit."},
            subcommands = {
                    FoodCommands.class, RecipeCommands.class, CrateCommands.class, PicocliCommands.ClearScreen.class, CommandLine.HelpCommand.class
            } // los comandos .class indica que estan implementados en cada una de sus clases
    )
    static class CliCommands implements Runnable {
        PrintWriter out;

        CliCommands() {}

        public void setReader(LineReader reader){
            out = reader.getTerminal().writer();
        }
        //Configura un escritor (PrintWriter) para imprimir la salida de la aplicación.

        public void run() {
            out.println(new CommandLine(this).getUsageMessage());
        } //Imprime la ayuda del comando raíz cuando se ejecuta sin argumentos.
    }


    public static void main(String[] args)
    {
        //new CommandLine(new BienvenidaCommand()).execute("-h");
        AnsiConsole.systemInstall();
        try {
            Supplier<Path> workDir = () -> Paths.get(System.getProperty("user.dir"));
            // set up JLine built-in commands
            //Builtins: Son comandos predefinidos de JLine, como top (monitoreo del sistema).
            //rename: Renombra un comando (ttop a top).
            //alias: Crea alias para comandos (zle como alias de widget).
            Builtins builtins = new Builtins(workDir, null, null);
            builtins.rename(Builtins.Command.TTOP, "top");
            builtins.alias("zle", "widget");
            builtins.alias("bindkey", "keymap");
            // set up picocli commands
            CliCommands commands = new CliCommands();

            PicocliCommands.PicocliCommandsFactory factory = new PicocliCommands.PicocliCommandsFactory();
            // Or, if you have your own factory, you can chain them like this:
            // MyCustomFactory customFactory = createCustomFactory(); // your application custom factory
            // PicocliCommandsFactory factory = new PicocliCommandsFactory(customFactory); // chain the factories

            //Es una instancia de la clase CliCommands que contiene los subcomandos.
            //factory permite la creación de comandos de Picocli. Esto facilita inyectar dependencias u objetos personalizados en los comandos.
            CommandLine cmd = new CommandLine(commands, factory);
            PicocliCommands picocliCommands = new PicocliCommands(cmd);


            //JLine es una biblioteca para construir interfaces de línea de comandos interactivas.
            Parser parser = new DefaultParser();
            try (Terminal terminal = TerminalBuilder.builder().build()) {
                SystemRegistry systemRegistry = new SystemRegistryImpl(parser, terminal, workDir, null); //systemRegistry: Administra los comandos disponibles (tanto los integrados de JLine como los de Picocli)
                systemRegistry.setCommandRegistries(builtins, picocliCommands);
                systemRegistry.register("help", picocliCommands);

                // LineReader: Maneja la entrada del usuario, completado de comandos, y sugerencias.
                LineReader reader = LineReaderBuilder.builder()
                        .terminal(terminal)
                        .completer(systemRegistry.completer()) //completer: Proporciona el completado de comandos con base en los comandos registrados.
                        .parser(parser)
                        .variable(LineReader.LIST_MAX, 50)   // max tab completion candidates
                        .build();
                builtins.setLineReader(reader);
                commands.setReader(reader);
                factory.setTerminal(terminal);
                //TailTipWidgets: Muestra sugerencias sobre los comandos mientras el usuario escribe.
                //TipType.COMPLETER: Ofrece las sugerencias basándose en el autocompletado de comandos.
                TailTipWidgets widgets = new TailTipWidgets(reader, systemRegistry::commandDescription, 5, TailTipWidgets.TipType.COMPLETER);
                widgets.enable();
                KeyMap<Binding> keyMap = reader.getKeyMaps().get("main");
                keyMap.bind(new Reference("tailtip-toggle"), KeyMap.alt("s"));

                String prompt = "prompt> ";
                String rightPrompt = null;

                // start the shell and process input until the user quits with Ctrl-D
                String line;
                while (true) {
                    try {
                        systemRegistry.cleanUp();
                        line = reader.readLine(prompt, rightPrompt, (MaskingCallback) null, null);
                        systemRegistry.execute(line);
                    } catch (UserInterruptException e) {
                        // Ignore
                    } catch (EndOfFileException e) {
                        return;
                    } catch (Exception e) {
                        systemRegistry.trace(e);
                    }
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            AnsiConsole.systemUninstall();
        }
    }
}
