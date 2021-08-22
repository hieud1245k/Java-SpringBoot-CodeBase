package com.fcn.fanscoin.blueprint;

import com.fcn.fanscoin.domain.Account;
import com.fcn.fanscoin.enums.CountryCode;
import com.github.javafaker.Faker;
import com.tobedevoured.modelcitizen.annotation.Blueprint;
import com.tobedevoured.modelcitizen.annotation.Default;
import com.tobedevoured.modelcitizen.callback.FieldCallback;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Blueprint(Account.class)
public class AccountBlueprint {
    private final Faker faker = new Faker();

    @Default
    private FieldCallback<Long> userId = new FieldCallback<Long>() {
        @Override
        public Long get(final Object referenceModel) {
            return faker.random().nextLong();
        }
    };

    @Default
    private FieldCallback<String> email = new FieldCallback<String>() {
        @Override
        public String get(final Object referenceModel) {
            return String.valueOf(faker.internet().emailAddress());
        }
    };

    @Default
    private String phoneNumber = faker.phoneNumber().subscriberNumber(15);

    @Default
    private String password = faker.internet().password();

    @Default
    private CountryCode countryCode = CountryCode.VN;
}
