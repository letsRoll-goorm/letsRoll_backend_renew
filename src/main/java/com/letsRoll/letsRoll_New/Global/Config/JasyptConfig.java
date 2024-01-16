package com.letsRoll.letsRoll_New.Global.Config;

import lombok.RequiredArgsConstructor;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class JasyptConfig {

    @Value("${jasypt.encryptor.password}")
    String password;

    @Bean(name = "jasyptStringEncryptor")
    public StandardPBEStringEncryptor stringEncryptor() {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(password); //암호화 및 복호화에 사용할 비밀 키를 설정한다. 외부에 노출되어서는 안된다.
        encryptor.setAlgorithm("PBEWithMD5AndDES"); //암호화에서 사용할 알고리즘을 설정한다.

        return encryptor;
    }
}
