package faang.school.accountservice.scheduler;

import faang.school.accountservice.service.PercentCalculationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class SchedulerCalculationPercentTest {
    @InjectMocks
    private SchedulerCalculationPercent scheduler;

    @Mock
    private PercentCalculationService percentCalculationService;

    @Test
    public void testCalculateLastUpdatedPercent() {
        scheduler.calculateLastUpdatedPercent();

        verify(percentCalculationService).calculateInterestAndUpdateBalances();
    }

    @Test
    public void testCalculateLastUpdatedPercentWithZeroCron() {
        doNothing().when(percentCalculationService).calculateInterestAndUpdateBalances();

        scheduler.calculateLastUpdatedPercent();

        verify(percentCalculationService, times(1)).calculateInterestAndUpdateBalances();
    }
}