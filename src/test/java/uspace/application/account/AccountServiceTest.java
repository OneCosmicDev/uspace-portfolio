package uspace.application.account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.domain.account.*;
import uspace.domain.account.exceptions.AccountCannotBeDeletedException;
import uspace.domain.account.exceptions.AccountNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    private static final String ANY_USERNAME_STR = "a_username";
    private static final AccountUsername ANY_USERNAME = new AccountUsername(ANY_USERNAME_STR);
    private static final String ANY_HOME_PLANET_STR = "a_home_planet";
    private static final HomePlanetName ANY_HOME_PLANET = new HomePlanetName(ANY_HOME_PLANET_STR);
    private static final Account ANY_ACCOUNT = new Account(ANY_USERNAME, ANY_HOME_PLANET);
    private AccountService accountService;
    @Mock
    private AccountRepository accountRepositoryMock;
    @Mock
    private AccountAssembler accountAssemblerMock;
    @Mock
    private AccountDeletionValidator accountDeletionValidatorMock;


    @BeforeEach
    void createAccountService() {
        accountService = new AccountService(accountRepositoryMock, accountAssemblerMock, accountDeletionValidatorMock);
    }

    @Test
    void givenNonExistingAccount_whenDeleteAccount_thenThrowsAccountNotFoundException() {
        givenNonExistingAccount();

        Executable deleteAccount = () -> accountService.deleteAccount(ANY_USERNAME_STR);

        assertThrows(AccountNotFoundException.class, deleteAccount);
    }

    @Test
    void givenExistingAccount_whenDeleteAccount_thenValidatesDeletion() {
        givenExistingAccount();

        accountService.deleteAccount(ANY_USERNAME_STR);

        verify(accountDeletionValidatorMock).validateDeletion(ANY_ACCOUNT);
    }

    @Test
    void givenExistingAccount_whenDeleteAccount_thenDeletesAccount() {
        givenExistingAccount();

        accountService.deleteAccount(ANY_USERNAME_STR);

        verify(accountRepositoryMock).delete(ANY_ACCOUNT);
    }

    @Test
    void givenExistingAccountAndDeletionValidatorThrows_whenDeleteAccount_thenDoNotDeleteAccount() {
        givenExistingAccount();
        doThrow(new AccountCannotBeDeletedException()).when(accountDeletionValidatorMock).validateDeletion(ANY_ACCOUNT);

        Executable deleteAccount = () -> accountService.deleteAccount(ANY_USERNAME_STR);

        assertThrows(AccountCannotBeDeletedException.class, deleteAccount);
        verify(accountRepositoryMock, never()).delete(ANY_ACCOUNT);
    }

    private void givenNonExistingAccount() {
        when(accountRepositoryMock.findByUsername(ANY_USERNAME)).thenReturn(null);
    }

    private void givenExistingAccount() {
        when(accountRepositoryMock.findByUsername(ANY_USERNAME)).thenReturn(ANY_ACCOUNT);
    }

}