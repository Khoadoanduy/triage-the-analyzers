package team.projectpulse.team;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TeamSpecsTest {

    @Test
    void containsTeamNameUsesLocaleIndependentCaseConversion() {
        Locale originalLocale = Locale.getDefault();
        try {
            Locale.setDefault(Locale.forLanguageTag("tr-TR"));
            CriteriaBuilder criteriaBuilder = mock(CriteriaBuilder.class);
            Root<Team> root = mock(Root.class);
            CriteriaQuery<?> query = mock(CriteriaQuery.class);
            Path<String> teamName = mock(Path.class);
            Expression<String> loweredTeamName = mock(Expression.class);
            Predicate predicate = mock(Predicate.class);
            when(root.<String>get("teamName")).thenReturn(teamName);
            when(criteriaBuilder.lower(teamName)).thenReturn(loweredTeamName);
            when(criteriaBuilder.like(loweredTeamName, "%istanbul%")).thenReturn(predicate);

            Predicate actual = TeamSpecs.containsTeamName("ISTANBUL").toPredicate(root, query, criteriaBuilder);

            assertThat(actual).isSameAs(predicate);
            verify(criteriaBuilder).like(loweredTeamName, "%istanbul%");
        } finally {
            Locale.setDefault(originalLocale);
        }
    }
}
