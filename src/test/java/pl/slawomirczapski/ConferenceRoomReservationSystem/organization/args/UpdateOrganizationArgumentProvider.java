package pl.slawomirczapski.ConferenceRoomReservationSystem.organization.args;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import pl.slawomirczapski.ConferenceRoomReservationSystem.organization.Organization;

import java.util.stream.Stream;

public class UpdateOrganizationArgumentProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return Stream.of(
                //name
                //existing arg
                //to update
                //expected
                Arguments.of(
                        "test1",
                        new Organization(1L, "test1", "desc1"),
                        new Organization(1L, null, "updated_desc_1"),
                        new Organization(1L, "test1", "updated_desc_1")
                ),
                Arguments.of(
                        "test1",
                        new Organization(1L, "test1", "desc1"),
                        new Organization(1L, "test2", null),
                        new Organization(1L, "test2", "desc1")
                ),
                Arguments.of(
                        "test1",
                        new Organization(1L, "test1", "desc1"),
                        new Organization(1L, "test2", "updated_desc_1"),
                        new Organization(1L, "test2", "updated_desc_1")
                ),
                Arguments.of(
                        "test1",
                        new Organization(1L, "test1", "desc1"),
                        new Organization(1L, null, null),
                        new Organization(1L, "test1", "desc1")
                )
        );
    }
}
