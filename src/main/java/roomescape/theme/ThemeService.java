package roomescape.theme;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public Theme save(Theme theme) {
        return themeRepository.save(theme);
    }

    public List<Theme> findAll() {
        return themeRepository.findAll();
    }

    public void deleteById(Long id) {
        themeRepository.deleteById(id);
    }
}
