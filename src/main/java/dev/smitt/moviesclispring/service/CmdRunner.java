package dev.smitt.moviesclispring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

@RequiredArgsConstructor
@Component
public class CmdRunner implements CommandLineRunner, ExitCodeGenerator {

    private int exitCode;

    private final MainCommand mainCommand;
    private final CommandLine.IFactory factory;

    @Override
    public void run(String... args) {
        exitCode = new CommandLine(mainCommand, factory).execute(args);
    }

    @Override
    public int getExitCode() {
        return exitCode;
    }

}
