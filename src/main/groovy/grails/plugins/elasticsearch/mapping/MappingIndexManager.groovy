package grails.plugins.elasticsearch.mapping

import grails.core.GrailsApplication
import grails.plugins.elasticsearch.exception.MappingException
import grails.plugins.elasticsearch.util.ElasticSearchConfigAware
import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import static grails.plugins.elasticsearch.mapping.MappingIndexStrategy.*

/**
 * Created by @antonio-latronico on 13/01/2022.
 */
@CompileStatic
class MappingIndexManager implements ElasticSearchConfigAware {
    GrailsApplication grailsApplication

    private static final Logger LOG = LoggerFactory.getLogger(this)

    private static final String DOT = "."

    String buildIndexName(ConfigObject indexMappingConfig, SearchableClassMapping scm){
        if (indexMappingConfig){
            String strategy = indexMappingConfig.getProperty('strategy') as String
            String prefix = indexMappingConfig.getProperty('prefix') as String

            if (!strategy || strategy.trim().isEmpty()){
                throw new MappingException("Mapping index strategy value '${strategy}' not allowed, allowed values are ['none', 'onlyAlias', 'prefixWithAlias', 'prefixWithPackage', 'prefixWithoutPackage']. ")
            }

            LOG.debug("Building index name mapping with strategy => ['${strategy}'] and prefix => ['${prefix}'].")

            switch (strategy.trim()) {
                case onlyAlias.name():
                    try {
                        //validateAlias("alias here", scm, MappingIndexStrategy.valueOf(strategy))
                        LOG.error("Strategy ${strategy} NOT YET IMPLEMENTED . . .")
                        throw new MappingException("Strategy ${strategy} NOT YET IMPLEMENTED . . .")
                    } catch(IllegalArgumentException e){
                        LOG.warn("Could not install mapping ${scm.indexName}/${scm.elasticTypeName} due to ${e.message}, index alias issue.")
                        throw new MappingException(e?.message)
                    }
                    return "alias_here".toLowerCase()
                    break;
                case prefixWithAlias.name():
                    try {
                        //validatePrefix(prefix, scm, MappingIndexStrategy.valueOf(strategy))
                        //validateAlias("alias here", scm, MappingIndexStrategy.valueOf(strategy))
                        LOG.error("Strategy ${strategy} NOT YET IMPLEMENTED . . .")
                        throw new MappingException("Strategy ${strategy} NOT YET IMPLEMENTED . . .")
                    } catch(IllegalArgumentException e){
                        LOG.warn("Could not install mapping ${scm.indexName}/${scm.elasticTypeName} due to ${e.message}, prefix or index alias issues.")
                        throw new MappingException(e?.message)
                    }
                    return prefix.toLowerCase() + DOT + "alias_here".toLowerCase()
                    break;
                case prefixWithPackage.name():
                    try {
                        validatePrefix(prefix, scm, MappingIndexStrategy.valueOf(strategy))
                    }catch(IllegalArgumentException e){
                        LOG.warn("Could not install mapping ${scm.indexName}/${scm.elasticTypeName} due to ${e.message}, prefix issue.")
                        throw new MappingException(e?.message)
                    }
                    return prefix.toLowerCase() + DOT + scm.domainClass.fullName.toLowerCase()
                    break;
                case prefixWithoutPackage.name():
                    try {
                        validatePrefix(prefix, scm, MappingIndexStrategy.valueOf(strategy))
                    }catch(IllegalArgumentException e){
                        LOG.warn("Could not install mapping ${scm.indexName}/${scm.elasticTypeName} due to ${e.message}, prefix issue.")
                        throw new MappingException(e?.message)
                    }
                    return prefix.toLowerCase() + DOT + scm.domainClass.defaultPropertyName.toLowerCase()
                    break;
                case none.name():
                    return scm.domainClass.fullName.toLowerCase()
                    break;
                default:
                    throw new MappingException("Mapping index strategy value '${strategy}' not allowed, allowed values are ['none', 'onlyAlias', 'prefixWithAlias', 'prefixWithPackage', 'prefixWithoutPackage']. ")
            }
        }
        scm.domainClass.fullName.toLowerCase()
    }

    void validateAlias(String alias, SearchableClassMapping scm, MappingIndexStrategy strategy){
        if (!alias){
            throw new IllegalArgumentException("Could not install mapping ${scm.indexName}/${scm.elasticTypeName}, " +
                    " index alias in domain class and searchable section has not been found  for the selected strategy ${strategy.name()}.")
        }
        else if (alias.trim().isEmpty()){
            throw new IllegalArgumentException("Could not install mapping ${scm.indexName}/${scm.elasticTypeName}, " +
                    " index alias in domain class and searchable section could not be empty for the selected strategy ${strategy.name()}.")
        }
    }

    void validatePrefix(String prefix, SearchableClassMapping scm, MappingIndexStrategy strategy){
        if (!prefix){
            throw new IllegalArgumentException("Could not install mapping ${scm.indexName}/${scm.elasticTypeName}, " +
                    " index prefix 'index.mapping.prefix' is mandatory for the selected strategy ${strategy.name()}.")
        }
        else if (prefix.trim().isEmpty()){
            throw new IllegalArgumentException("Could not install mapping ${scm.indexName}/${scm.elasticTypeName}, " +
                    " index prefix 'index.mapping.prefix' could not be empty for the selected strategy ${strategy.name()}.")
        }
        else if (prefix.toLowerCase().trim() == "none"){
            throw new IllegalArgumentException("Could not install mapping ${scm.indexName}/${scm.elasticTypeName}, " +
                    " index prefix 'index.mapping.prefix' should be valid and '${prefix.toLowerCase().trim()}' is not accepted for the selected strategy ${strategy.name()}.")
        }
    }
}
