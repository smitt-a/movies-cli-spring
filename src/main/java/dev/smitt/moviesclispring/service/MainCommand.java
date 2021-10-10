package dev.smitt.moviesclispring.service;

import org.springframework.stereotype.Component;

import static picocli.CommandLine.*;

@Component
@Command(name = "movies", mixinStandardHelpOptions = true, subcommands = {FfmpegCommand.class})
public class MainCommand {
}
