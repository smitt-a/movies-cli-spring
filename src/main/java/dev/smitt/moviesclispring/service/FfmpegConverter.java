package dev.smitt.moviesclispring.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.Executor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class FfmpegConverter {

    private static final Pattern EPISODE_PATTERN = Pattern.compile("^(.*?)S\\d\\dE\\d\\d");
    private static final String COMMAND_TEMPLATE = "${ffmpegExecutable} -i \"${originalFile}\" -map 0 -vcodec copy -scodec copy -acodec ac3 -b:a 640k \"${destination}\"";

    private final Executor commandExecutor;

    public void convertEpisode(Path ffpmegExecutable, Path inputFile) {
        System.out.println("Converting episode: " + inputFile);
        var originalFileName = inputFile.getFileName().toString();
        var matcher = EPISODE_PATTERN.matcher(originalFileName);
        Path destination;
        if (matcher.find()) {
            destination = inputFile.getParent().resolve(matcher.group(0) + ".mkv");
        } else {
            throw new RuntimeException("Failed to extract filename!");
        }

        convert(ffpmegExecutable, inputFile, destination);
    }

    private void convert(Path ffpmegExecutable, Path inputFile, Path destination) {
        var commandLine = CommandLine.parse(
                COMMAND_TEMPLATE,
                Map.of("ffmpegExecutable", ffpmegExecutable, "originalFile", inputFile.toString(), "destination", destination.toString()));

        try {
            commandExecutor.execute(commandLine);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
