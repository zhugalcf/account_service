package faang.school.accountservice.repository;

import org.testcontainers.containers.PostgreSQLContainer;

public class AccountNumberContainer extends PostgreSQLContainer<AccountNumberContainer> {
    private static final String IMAGE_VERSION = "postgres:13.3";
    private static AccountNumberContainer container;

    private AccountNumberContainer(){
        super(IMAGE_VERSION);
    }

    public static AccountNumberContainer getInstance(){
        if(container == null) {
            container = new AccountNumberContainer();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", container.getJdbcUrl());
        System.setProperty("DB_USERNAME", container.getUsername());
        System.setProperty("DB_PASSWORD", container.getPassword());
    }

    @Override
    public void stop() {
    }
}
