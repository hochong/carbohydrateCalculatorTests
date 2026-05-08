package listeners;

import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.HookTestStep;
import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.TestStepFinished;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Cucumber plugin that logs the execution time of every step and hook
 * to the console after it finishes.
 *
 * Output format:
 *   [STEP] <step pattern>                              |   432ms | PASSED
 *   [HOOK] @Before steps.CarbohydrateCalculatorSteps  |   215ms | PASSED
 *
 * Registered in TestRunner via plugin = { "listeners.StepTimingPlugin" }
 */
public class StepTimingPlugin implements ConcurrentEventListener {

    private static final Logger log = LoggerFactory.getLogger(StepTimingPlugin.class);

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestStepFinished.class, this::onStepFinished);
    }

    private void onStepFinished(TestStepFinished event) {
        long ms     = event.getResult().getDuration().toMillis();
        String status = event.getResult().getStatus().name();

        if (event.getTestStep() instanceof PickleStepTestStep step) {
            // Scenario step — log the step definition expression (pattern)
            String pattern = step.getDefinitionMatch().getPattern();
            log.info(String.format("[STEP] %-65s | %5dms | %s", pattern, ms, status));

        } else if (event.getTestStep() instanceof HookTestStep hook) {
            // Before / After hook — log the hook type and code location
            String hookType = hook.getHookType().name();
            String location = hook.getCodeLocation();
            log.info(String.format("[HOOK] %-8s %-56s | %5dms | %s", "@" + hookType, location, ms, status));
        }
    }
}
