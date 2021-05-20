package hu.progmasters.ujratervezes.week16.dailybugle.service;

import hu.progmasters.ujratervezes.week16.dailybugle.domain.Reader;
import hu.progmasters.ujratervezes.week16.dailybugle.dto.ReaderDto;
import hu.progmasters.ujratervezes.week16.dailybugle.repository.ReaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.List;

@Service
@Transactional
public class ReaderService {

    private final ReaderRepository readerRepository;
    private final Clock clock;

    @Autowired
    public ReaderService(ReaderRepository readerRepository, Clock clock) {
        this.readerRepository = readerRepository;
        this.clock = clock;
    }

    public List<Reader> getReaders() {
        return readerRepository.getReaders();
    }

    public Reader getReader(int id) {
        return null;
    }

    public boolean saveReader(ReaderDto data) {
        return false;
    }
}
