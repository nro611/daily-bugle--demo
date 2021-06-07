package hu.progmasters.ujratervezes.week16.dailybugle.service;

import hu.progmasters.ujratervezes.week16.dailybugle.domain.Publicist;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.PublicistDto;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.PublicistListDto;
import hu.progmasters.ujratervezes.week16.dailybugle.repository.PublicistRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j(topic = "PublicistService")
public class PublicistService {

    private final PublicistRepository publicistRepository;
    private final Clock clock;

    @Autowired
    public PublicistService(PublicistRepository publicistRepository, Clock clock) {
        this.publicistRepository = publicistRepository;
        this.clock = clock;
    }

    public List<PublicistListDto> getPublicists() {
        return publicistRepository.getPublicists();
    }

    public boolean savePublicist(PublicistDto data) {
        return publicistRepository.savePublicist(data, LocalDateTime.now(clock));

    }

    public Publicist getPublicist(int id) {
        return publicistRepository.getPublicist(id);
    }

    public boolean updatePublicist(int id, PublicistDto data) {
        return publicistRepository.updatePublicist(id, data, LocalDateTime.now(clock));
    }

    public boolean deletePublicist(int id) {
        return publicistRepository.deletePublicist(id, LocalDateTime.now(clock));
    }


    public boolean getPhonebook() {
        boolean successfulWrite = false;
        List<String> lines = publicistRepository.getPhonebook().stream()
                .map(phonebookDto -> phonebookDto.getName() + ";" + phonebookDto.getPhone())
                .collect(Collectors.toList());
        createDirectoryIfNotExists();

        try (BufferedWriter writer = Files.newBufferedWriter(Path.of("files/phonebook.csv"))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
            successfulWrite = true;
        } catch (IOException ioException) {
            logger.error(ioException.getMessage());
        }
        return successfulWrite;
    }

    private void createDirectoryIfNotExists() {
        File directory = new File("files");
        if (!directory.exists()) {
            directory.mkdir();
        }
    }
}
