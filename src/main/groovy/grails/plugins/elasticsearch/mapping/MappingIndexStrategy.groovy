package grails.plugins.elasticsearch.mapping

import groovy.transform.CompileStatic

/**
 * Created by @antonio-latronico on 13/01/2022.
 */
@CompileStatic
enum MappingIndexStrategy {
    none, onlyAlias, prefixWithAlias, prefixWithPackage, prefixWithoutPackage
}