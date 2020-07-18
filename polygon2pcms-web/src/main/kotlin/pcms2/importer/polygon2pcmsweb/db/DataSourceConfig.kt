package pcms2.importer.polygon2pcmsweb.db

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pcms2.importer.polygon2pcmsweb.AppConfig

@Configuration
class DataSourceConfig @Autowired constructor(val config: AppConfig) {

    @Bean
    fun getDataSource() = DataSourceBuilder
                .create()
                .url(config.datasourceUrl)
                .username(config.datasourceUsername)
                .password(config.datasourcePassword)
                .build()
}