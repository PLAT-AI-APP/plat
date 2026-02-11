package com.plat.platdata.entity.character;

import com.plat.platdata.entity.character.lorebook.Lorebook;
import com.plat.platdata.entity.character.lorebook.LorebookTranslation;
import com.plat.platdata.jparepository.LorebookRepository;
import com.plat.platdata.jparepository.LorebookTranslationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class LorebookTest {

    @Autowired
    private LorebookRepository lorebookRepository;
    @Autowired
    private LorebookTranslationRepository lorebookTranslationRepository;

    @Test
    void 로어북생성() {
        // given
        var ko = LorebookTranslation.builder()
            .keywords(List.of("A", "B", "C"))
            .content("어쩌구")
            .build();

        var saveLorebook = Lorebook.builder()
            .lorebookTranslations(List.of(ko))
            .build();

        // when
        Lorebook lorebook = lorebookRepository.save(saveLorebook);
        Optional<Lorebook> find = lorebookRepository.findById(lorebook.getId());
        // then
        assertThat(find).isNotEmpty();
        assertThat(find.get().getLorebookTranslations().size()).isEqualTo(1);

    }

}
