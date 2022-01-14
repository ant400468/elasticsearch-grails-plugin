elasticSearch {
    /**
     * Date formats used by the unmarshaller of the JSON responses
     */
    date.formats = ["yyyy-MM-dd'T'HH:mm:ss.S'Z'"]

    /**
     * Hosts for remote ElasticSearch instances.
     * Will only be used with the "transport" client mode.
     * If the client mode is set to "transport" and no hosts are defined, ["localhost", 9300] will be used by default.
     */
    client.hosts = [
            [host: 'localhost', port: 9300]
    ]

    /**
     * Default mapping property exclusions
     *
     * No properties matching the given names will be mapped by default
     * ie, when using "searchable = true"
     *
     * This does not apply for classes using mapping by closure
     */
    defaultExcludedProperties = ['password']

    /**
     * Determines if the plugin should reflect any database save/update/delete automatically
     * on the ES instance. Default to false.
     */
    disableAutoIndex = false

    /**
     * Should the database be indexed at startup.
     *
     * The value may be a boolean true|false.
     * Indexing is always asynchronous (compared to Searchable plugin) and executed after BootStrap.groovy.
     */
    bulkIndexOnStartup = true

    /**
     *  Max number of requests to process at once. Reduce this value if you have memory issue when indexing a big amount of data
     *  at once. If this setting is not specified, 500 will be use by default.
     */
    maxBulkRequest = 500

    /**
     * Should component-mapped properties be unmarshalled. The default is true.
     */
    unmarshallComponents = true

    /**
     * The name of the ElasticSearch mapping configuration property that annotates domain classes. The default is 'searchable'.
     */
    searchableProperty.name = 'searchable'

    /**
     * The strategy to be used in case of a conflict installing mappings
     */
    migration.strategy = 'alias'

    /**
     * Whether to replace existing indices with aliases when there's a conflict and the 'alias' strategy is chosen
     */
    migration.aliasReplacesIndex = true

    /**
     * When set to false, in case of an alias migration, prevents the alias to point to the newly created index
     */
    migration.disableAliasChange = false

    index.settings.numberOfReplicas = 0

    /**
     * Custom Index mapping 'strategy' based on prefix and alias (to create custom index name for each domain class to be indexed into the cluster)
     *
     * Allowed strategy values are ['none', 'onlyAlias', 'prefixWithAlias', 'prefixWithPackage', 'prefixWithoutPackage']
     *
     * 1. 'none' set the index name of each root grails domain class to the full name (include package)
     * 2. 'onlyAlias' set the index name to the given property 'indexAlias' and attribute 'name' (must be defined in searchable section)
     * 3. 'prefixWithAlias' set the index name adding a mandatory prefix ('index.mapping.prefix') ahead the property 'indexAlias' (must be defined in searchable section)
     * 4. 'prefixWithPackage' set the index name of each root domain class adding a prefix  ahead the domain class full name (include package)
     * 5. 'prefixWithoutPackage' set the index name of each root domain class with the prefix ahead the only domain class name (exclude package)
     *
     *
     *  ##### Some ##### help  ### if ######## strategy ####### with ## alias ############
     *  If the strategy to set it is based on aliases ('onlyAlias' or 'prefixWithAlias')
     *
     *  The following steps are mandatory :
     *
     *  1. Set the strategy values in Elastic search config ('index.mapping.strategy','index.mapping.prefix','includeTransients = true')
     *  2. A new 'transient' property named exactly 'indexAlias' must be defined into each grails domain class to be indexed
     *  3. Set 'indexAlias' in 'searchable' section and attribute 'name'
     *
     *  Example strategy 'prefixWithAlias' to create in the ES cluster the following index name : "testa.another_index_name"
     *
     *  1. Elastic search configs
     *  index.mapping.strategy = 'prefixWithAlias'
     *  index.mapping.prefix = 'testa'
     *  includeTransients = true
     *
     *  2. Set transient 'indexAlias' property in grails domain class
     *
     *   static transients = ['indexAlias']
     *
     *   String indexAlias
     *   String getIndexAlias() { indexAlias?.toLowerCase() }
     *
     *  3. Set 'indexAlias' in searchable section
     *
     *  static searchable = {indexAlias name: "another_index_name", index: false ...other properties }
     *
     *  ##################################################
     *
     *
     *  If the strategy selected is 'prefixWithAlias', 'prefixWithPackage', 'prefixWithoutPackage'
     *  the 'prefix' ('index.mapping.prefix') is mandatory and should contain a valid value different than 'none'
     *
     *  If the strategy selected it is NOT based on 'aliases' the property 'indexAlias' it is optional and can be omitted
     *
     */
    index.mapping.strategy = 'none'
    index.mapping.prefix = 'none'

    /**
     * Whether to index and search all non excluded transient properties. All explicitly included transients in @only@ will be indexed regardless.
     */
    includeTransients = false

    /**
     * Disable dynamic method injection in domain class
     */
    disableDynamicMethodsInjection = false

    /**
     * Search method name in domain class, defaults to search
     */
    searchMethodName = "search"

    /**
     * countHits method name in domain class, defaults to search
     */
    countHitsMethodName = "countHits"

    plugin.mapperAttachment.enabled = true
}

environments {
    development {
        /**
         * Possible values : "local", "dataNode", "transport"
         * If set to null, "transport" mode is used by default.
         */
        elasticSearch.client.mode = 'local'
    }
    test {
        elasticSearch {
            client.mode = 'local'
            index.store.type = 'simplefs' // store local node in memory and not on disk
        }
    }
    production {
        elasticSearch.client.mode = 'node'
    }
}