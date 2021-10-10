package dev.smitt.moviesclispring.service;

import com.google.common.base.Stopwatch;
import dev.smitt.moviesclispring.util.Log;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.PropertiesDefaultProvider;

import java.nio.file.Files;
import java.nio.file.Path;

import static picocli.CommandLine.Command;

@RequiredArgsConstructor
@Component
@Command(name = "ffmpeg", mixinStandardHelpOptions = true)
public class FfmpegCommand implements Runnable {

    private final FfmpegConverter ffmpegConverter;

    @Option(names = {"-executable"}, description = "Ffmpeg executable path", defaultValue = "ffmpeg")
    private Path ffpmegExecutablePath;

    @Parameters(paramLabel = "<inputFolder>", description = "Input folder containg .mkv files to convert", index = "0")
    private String inputFolder;

    @Override
    public void run() {
        var sw = Stopwatch.createStarted();
        //TODO parallelize -> async commons-exec executor?
        try (var files = Files.list(Path.of(inputFolder))) {
            files
                    .filter(file -> file.getFileName().toString().endsWith(".mkv"))
                    .forEach(file -> ffmpegConverter.convertEpisode(ffpmegExecutablePath, file));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        var elapsed = sw.elapsed();

        Log.info("ffmpeg processing duration: " + elapsed);
    }

}
