package pl.slawomirczapski.ConferenceRoomReservationSystem.organization.args;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class ValidateAddOrganizationArgumentProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return Stream.of(
                Arguments.of(
                        """
                                                        {"name":"     ",
                                                        "description":"desc1"}
                                """,
                        "must not be blank"
                ),
                Arguments.of(
                        """
                                                        {"description":"desc1"}
                                """,
                        "must not be blank"
                ),
                Arguments.of(
                        """
                                                        {"name":"1",
                                                        "description":"desc1"}
                                """,
                        "size must be between 2 and 20"
                )

        );
    }
}
