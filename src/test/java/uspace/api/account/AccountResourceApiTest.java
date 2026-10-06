package uspace.api.account;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import uspace.api.exceptions.ErrorResponse;
import uspace.application.account.AccountService;
import uspace.domain.account.exceptions.AccountCannotBeDeletedException;
import uspace.domain.account.exceptions.AccountNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

@Tag("component")
class AccountResourceApiTest extends JerseyTest {
    private static final String ANY_USERNAME_STR = "a_username";
    private AccountService accountServiceMock;

    @Override
    protected Application configure() {
        accountServiceMock = mock(AccountService.class);
        AccountDtoValidator accountDtoValidatorMock = mock(AccountDtoValidator.class);
        return new ResourceConfig()
                .packages("uspace")
                .register(JacksonFeature.withoutExceptionMappers())
                .register(new AccountResource(accountServiceMock, accountDtoValidatorMock));
    }

    @Test
    void givenAccountNotFound_whenDeleteAccount_thenReturnAccountNotFoundError() {
        doThrow(new AccountNotFoundException()).when(accountServiceMock).deleteAccount(ANY_USERNAME_STR);

        Response response = callDeleteAccountApi();

        assertEquals(404, response.getStatus());
        ErrorResponse errorResponse = response.readEntity(ErrorResponse.class);
        assertEquals("ACCOUNT_NOT_FOUND", errorResponse.error);
    }

    @Test
    void givenAccountThatCannotBeDeleted_whenDeleteAccount_thenReturnAccountCannotBeDeletedError() {
        doThrow(new AccountCannotBeDeletedException()).when(accountServiceMock).deleteAccount(ANY_USERNAME_STR);

        Response response = callDeleteAccountApi();

        assertEquals(400, response.getStatus());
        ErrorResponse errorResponse = response.readEntity(ErrorResponse.class);
        assertEquals("ACCOUNT_CANNOT_BE_DELETED", errorResponse.error);
    }

    @Test
    void givenValidDeletion_whenDeleteAccount_thenReturnOk() {
        Response response = callDeleteAccountApi();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    private Response callDeleteAccountApi() {
        WebTarget target = target("/accounts/" + ANY_USERNAME_STR);
        return target.request().delete();
    }
}