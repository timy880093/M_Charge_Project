package unitTest.remaining_contract_renew;

import com.gateweb.charge.enumeration.ContractStatus;
import com.gateweb.orm.charge.entity.Contract;
import com.gateweb.utils.LocalDateTimeUtils;

import java.time.LocalDateTime;
import java.util.Optional;

public class TestContractProvider {
    public static Contract genNullPeriodMonthContract() {
        Contract contract = new Contract();
        contract.setContractId(13210L);
        contract.setCreateDate(LocalDateTime.now());
        contract.setName("關網");
        contract.setCompanyId(8367L);
        contract.setPackageId(28L);
        contract.setAutoRenew(true);
        Optional<LocalDateTime> effectiveDateOpt = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2022-02-12T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );
        contract.setEffectiveDate(effectiveDateOpt.get());
        Optional<LocalDateTime> expirationDate = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2023-02-11T23:59:59.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );
        contract.setExpirationDate(expirationDate.get());
        contract.setCreateDate(LocalDateTime.now());
        contract.setStatus(ContractStatus.E);
        return contract;
    }

    public static Contract genValidContract() {
        Contract contract = new Contract();
        contract.setContractId(13210L);
        contract.setCreateDate(LocalDateTime.now());
        contract.setName("關網");
        contract.setCompanyId(8367L);
        contract.setPackageId(28L);
        contract.setAutoRenew(true);
        Optional<LocalDateTime> effectiveDateOpt = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2022-02-12T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );
        contract.setEffectiveDate(effectiveDateOpt.get());
        Optional<LocalDateTime> expirationDate = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2023-02-11T23:59:59.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );
        contract.setExpirationDate(expirationDate.get());
        contract.setPeriodMonth(12);
        contract.setCreateDate(LocalDateTime.now());
        contract.setStatus(ContractStatus.E);
        return contract;
    }
}
