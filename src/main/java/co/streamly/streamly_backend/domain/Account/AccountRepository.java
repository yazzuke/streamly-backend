package co.streamly.streamly_backend.domain.Account;

import co.streamly.streamly_backend.domain.Account.Account;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByServiceName(String serviceName);

}