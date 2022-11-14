package helsinki.test_config;

import static org.apache.commons.lang.StringUtils.isEmpty;

import java.util.Optional;
import java.util.Properties;

import ua.com.fielden.platform.test.DbCreator;
import ua.com.fielden.platform.test.IDomainDrivenTestCaseConfiguration;
import ua.com.fielden.platform.test.db_creators.H2DbCreator;
import ua.com.fielden.platform.test.runners.AbstractDomainDrivenTestCaseRunner;

/**
 * A test runner that selects a test configuration {@link ITestContext} from either {@link H2DomainDrivenTestCaseRunner} or {@link PostgresqlDomainDrivenTestCaseRunner} for running unit test.
 * The criteria for selecting the appropriate test runner is based on runtime settings.
 *
 * @author Generated
 */
public class H2OrPostgresqlContextSelector extends AbstractDomainDrivenTestCaseRunner {

    // Note: This assumes PostgreSQL is listening on port 5432 (the default).
    // There is not much else in the URI that would uniquely identify that we are connecting to a PostgreSQL database.
    private static final boolean POSTGRESQL = !isEmpty(System.getProperty("databaseUri")) && System.getProperty("databaseUri").contains("5432");

    public H2OrPostgresqlContextSelector(final Class<?> klass) throws Exception {
        super(klass, POSTGRESQL ? PostgresqlDbCreator.class : H2DbCreator.class, Optional.empty());
    }

    public H2OrPostgresqlContextSelector(final Class<?> klass, final Class<? extends DbCreator> dbCreatorType, final Optional<IDomainDrivenTestCaseConfiguration> testConfig) throws Exception {
        super(klass, dbCreatorType, testConfig);
    }

    @Override
    protected Properties mkDbProps(final String dbUri) {
        return selectRunnerConfig().mkDbProps(dbUri);
    }

    @Override
    public void dbCleanUp() {
        super.dbCleanUp();
        selectRunnerConfig().dbCleanUp();
    }

    private ITestContext selectRunnerConfig() {
        return POSTGRESQL ? new PostgresqlDomainDrivenTestCaseRunner.PostgresqlTestContext() : new H2DomainDrivenTestCaseRunner.H2TestContext(this);
    }

}
