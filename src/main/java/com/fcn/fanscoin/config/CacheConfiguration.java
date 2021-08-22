package com.fcn.fanscoin.config;

import com.fcn.fanscoin.domain.Account;
import com.fcn.fanscoin.domain.User;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

import javax.cache.CacheManager;

import java.time.Duration;

import static com.fcn.fanscoin.constant.Constant.USERS_BY_EMAIL_CACHE;
import static com.fcn.fanscoin.constant.Constant.USERS_BY_LOGIN_CACHE;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(final JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
                Eh107Configuration.fromEhcacheCacheConfiguration(
                        CacheConfigurationBuilder
                                .newCacheConfigurationBuilder(Object.class,
                                                              Object.class,
                                                              ResourcePoolsBuilder
                                                                      .heap(ehcache.getMaxEntries()))
                                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(
                                        Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                                .build());
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(final CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, USERS_BY_LOGIN_CACHE);
            createCache(cm, USERS_BY_EMAIL_CACHE);
            createCache(cm, User.class.getName());
            createCache(cm, Account.class.getName());
            createCache(cm, com.fcn.fanscoin.domain.User.class.getName() + ".authorities");
        };
    }

    private void createCache(final CacheManager cm, final String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(final GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(final BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
