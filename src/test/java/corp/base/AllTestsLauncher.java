package corp.base;

import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import java.io.PrintWriter;

public class AllTestsLauncher {
    public static void main(String[] args) {
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(
                        DiscoverySelectors.selectClass(NoteControllerTest.class),
                        DiscoverySelectors.selectClass(NoteServiceTest.class),
                        DiscoverySelectors.selectClass(UserServiceTest.class)
                )
                .build();

        Launcher launcher = LauncherFactory.create();

        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        launcher.registerTestExecutionListeners(listener);

        launcher.execute(request);

        TestExecutionSummary summary = listener.getSummary();
        summary.printTo(new PrintWriter(System.out));

        System.out.println("Results:");
        System.out.printf("Total tests: %d%n", summary.getTestsStartedCount());
        System.out.printf("Passed tests: %d%n", summary.getTestsSucceededCount());
        System.out.printf("Failed tests: %d%n", summary.getTestsFailedCount());

        if (summary.getTestsFailedCount() > 0) {
            System.out.println("Failed tests details:");
            summary.getFailures().forEach(failure -> {
                System.out.printf("Test %s failed: %s%n",
                        failure.getTestIdentifier().getDisplayName(),
                        failure.getException().getMessage());
            });
        }
    }
}
