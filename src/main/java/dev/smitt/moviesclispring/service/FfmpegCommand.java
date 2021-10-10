package dev.smitt.moviesclispring.service;

import com.google.common.base.Stopwatch;
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
@Command(name = "ffmpegconverter", mixinStandardHelpOptions = true, defaultValueProvider = PropertiesDefaultProvider.class)
public class FfmpegCommand implements Runnable {

    private final FfmpegConverter ffmpegConverter;

    @Option(names = {"-ffmpeg"}, description = "Ffmpeg executable path", defaultValue = "ffmpeg")
    private String ffpmegExecutablePath;

    @Parameters(paramLabel = "<inputFolder>", description = "Input folder containg .mkv files to convert", index = "0")
    private String inputFolder;

    @Override
    public void run() {
        var sw = Stopwatch.createStarted();
        //TODO parallelize -> async commons-exec executor?
        try (var files = Files.list(Path.of(inputFolder))) {
            files
                    .filter(file -> file.getFileName().toString().endsWith(".mkv"))
                    .forEach(file -> ffmpegConverter.convertEpisode(Path.of(ffpmegExecutablePath), file));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        var elapsed = sw.elapsed();

        System.out.println("ffmpeg processing duration: " + elapsed);
    }

}
