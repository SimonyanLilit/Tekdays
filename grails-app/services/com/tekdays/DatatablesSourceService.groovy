package com.tekdays

import grails.converters.JSON
import grails.transaction.Transactional
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.plugins.support.aware.GrailsApplicationAware
import org.hibernate.envers.AuditReaderFactory
import org.hibernate.envers.query.AuditQuery
import org.hibernate.SessionFactory



import javax.sql.DataSource

@Transactional
class DatatablesSourceService implements GrailsApplicationAware {

    GrailsApplication grailsApplication
    SessionFactory sessionFactory

    def dataTablesSource(propertiesToRender, entityName, params) {
        boolean someFilter = false

        Class clazz = grailsApplication.domainClasses.find { it.clazz.simpleName == entityName }.clazz

        def filters = []
        propertiesToRender.eachWithIndex { prop, idx ->
            def sSearchField = params["sSearch_${idx}"]
            if (sSearchField != '') {
                someFilter = true
                filters << "dt.${prop} = '${sSearchField}'"
            }
            if (params.sSearch) {
                if (params["bSearchable_${idx}"] == 'true') {
                    filters << "dt.${prop} like :filter"
                }
            }
        }
        def filter = filters.join(" OR ")

        def dataToRender = [:]

        dataToRender.sEcho = params.sEcho
        dataToRender.aaData = []  // Array of data.

        dataToRender.iTotalRecords = clazz.count()
        dataToRender.iTotalDisplayRecords = dataToRender.iTotalRecords

        def query = new StringBuilder("from ${entityName} as dt where dt.id is not null")
        def appendToQuery = ""

        query.append(appendToQuery)
        if (params.sSearch) {
            query.append(" and (${filter})")
        } else if (someFilter) {
            query.append(" and (${filter})")
        }
        def sortingCols = params?.iSortingCols as int
        def orderBy = new StringBuilder()
        (0..sortingCols - 1).each {
            if (it > 0) {
                orderBy.append(",")
            }
            def sortDir = params["sSortDir_${it}"]?.equalsIgnoreCase('asc') ? 'asc' : 'desc'

            def sortProperty = propertiesToRender[params["iSortCol_${it}"] as int]
            orderBy.append("dt.${sortProperty} ${sortDir}")
        }
        query.append(" order by ${orderBy}")

        def records
        if (params.sSearch) {
            String sSearch = params.sSearch
            // Revise the number of total display records after applying the filter
            def countQuery = new StringBuilder("select count(*) from ${entityName} as dt where dt.id is not null")
            countQuery.append(appendToQuery).append(" and (${filter})")
            def result = clazz.executeQuery(countQuery.toString(), [filter: "%${sSearch}%"])
            if (result) {
                dataToRender.iTotalDisplayRecords = result[0]
            }
            records = clazz.findAll(query.toString(), [filter: "%${sSearch}%"], [max: params.iDisplayLength as int, offset: params.iDisplayStart as int])
        } else if (someFilter) {
            // Revise the number of total display records after applying the filter
            def countQuery = new StringBuilder("select count(*) from ${entityName} as dt where dt.id is not null")
            countQuery.append(appendToQuery).append(" and (${filter})")
            def result = clazz.executeQuery(countQuery.toString())
            if (result) {
                dataToRender.iTotalDisplayRecords = result[0]
            }
            records = clazz.findAll(query.toString(), [max: params.iDisplayLength as int, offset: params.iDisplayStart as int])
        } else {
            // Revise the number of total display records after applying the filter
            def countQuery = new StringBuilder("select count(*) from ${entityName} as dt where dt.id is not null")
            countQuery.append(appendToQuery)
            def result = clazz.executeQuery(countQuery.toString())
            if (result) {
                dataToRender.iTotalDisplayRecords = result[0]
            }
            records = clazz.findAll(query.toString(), [max: params.iDisplayLength as int, offset: params.iDisplayStart as int])
        }

        records?.each { r ->
            def data = []
            propertiesToRender.each { f ->
                data.add((r["${f}"] instanceof BigDecimal) ? r["${f}"] : r["${f}"]?.toString())
            }
            dataToRender.aaData << data
        }

        return dataToRender as JSON
    }
//    def getAudit(id) {
//        def sql = new Sql(dataSource)
//        String query = "select a.full_name, a.email, a.website, a.bio, a.date_created, a.last_updated,\n" +
//                "    r.current_user_id as changedBy\n" +
//                "FROM\n" +
//                "    audited_tek_user as a\n" +
//                "        join \n" +
//                "    user_revision_entity as r on r.id = a.rev\n" +
//                "        join \n" +
//                "    tek_user as dt on dt.id = a.id\n" +
//                "where \n" +
//                "    dt.id =${id}"
//        def result = sql.rows(query)
//        sql.close()
//        return result
//    }
//    def revisions() {
//        def auditQueryCreator = AuditReaderFactory.get().createQuery()
//
//        def result = []
//        AuditQuery query = auditQueryCreator.forRevisionsOfEntity(TekUser.class, false, true)
//        query.resultList.each {
//            if(it[0].id==params.getLong('id')) {
//                result.add(it)
//            }
//        }
//        [result: result]
//    }

    def dataTablesSourceForRevisions(propertiesToRender, params) {
        boolean someFilter = false

        //clazz

        def filters = []
        propertiesToRender.eachWithIndex { prop, idx ->
            def sSearchField = params["sSearch_${idx}"]
            if (sSearchField != '') {
                someFilter = true
                filters << "dtRevision.${prop} = '${sSearchField}'"
            }
            if (params.sSearch) {
                if (params["bSearchable_${idx}"] == 'true') {
                    filters << "dtRevision.${prop} like :filter"
                }
            }
        }
        def filter = filters.join(" OR ")
        def dataToRender = [:]
        dataToRender.sEcho =params.sEcho
        def records = []
        def auditQueryCreator = AuditReaderFactory.get(sessionFactory.currentSession).createQuery()
        AuditQuery query = auditQueryCreator.forRevisionsOfEntity(TekUser.class, false, true)
        query.resultList.each {
            if(it[0].id==params?.tekUserId as Long) {


                records.add([ it[0]?.fullName, it[0]?.email, it[0]?.website, it[0]?.bio, it[0]?.dateCreated.toString(), it[0]?.lastUpdated.toString(),  it[1]?.currentUser.fullName,it[1]?.currentUser.id])
            }
        }
        dataToRender.aaData = records// Array of data.
        dataToRender.iTotalRecords = records.size()
        dataToRender.iTotalDisplayRecords = dataToRender.iTotalRecords











//        def query = new StringBuilder("from ${entityName} as dt where dt.id is not null")
//        def appendToQuery = ""
//
//        query.append(appendToQuery)
//        if (params.sSearch) {
//            query.append(" and (${filter})")
//        } else if (someFilter) {
//            query.append(" and (${filter})")
//        }
//        def sortingCols = params?.iSortingCols as int
//        def orderBy = new StringBuilder()
//        (0..sortingCols - 1).each {
//            if (it > 0) {
//                orderBy.append(",")
//            }
//            def sortDir = params["sSortDir_${it}"]?.equalsIgnoreCase('asc') ? 'asc' : 'desc'
//
//            def sortProperty = propertiesToRender[params["iSortCol_${it}"] as int]
//            orderBy.append("dt.${sortProperty} ${sortDir}")
//        }
//        query.append(" order by ${orderBy}")

      //  def records

//            String sSearch = params.sSearch
//            // Revise the number of total display records after applying the filter
//            def countQuery = new StringBuilder("select count(*) from ${entityName} as dt where dt.id is not null")
//            countQuery.append(appendToQuery).append(" and (${filter})")
//            def result = clazz.executeQuery(countQuery.toString(), [filter: "%${sSearch}%"])
//            if (result) {
//                dataToRender.iTotalDisplayRecords = result[0]
//            }






//        records?.each { r ->
//            def data = []
//            propertiesToRender.each { f ->
//                data.add((r["${f}"] instanceof BigDecimal) ? r["${f}"] : r["${f}"]?.toString())
//            }
//        }

        return dataToRender as JSON
    }

















    def dataTablesSourceForAudit(propertiesToRender, params) {
        def records = []

        // Create the audit query
        def auditQueryCreator = AuditReaderFactory.get(sessionFactory.currentSession).createQuery()
        AuditQuery auditQuery = auditQueryCreator.forRevisionsOfEntity(TekUser.class, false, true)

        // Filtering based on tekUserId
        auditQuery.resultList.each {
            if (it[0].id == params?.tekUserId as Long) {
                records.add([
                        it[0]?.fullName,
                        it[0]?.email,
                        it[0]?.website,
                        it[0]?.bio,
                        it[0]?.dateCreated.toString(),
                        it[0]?.lastUpdated.toString(),
                        it[1]?.currentUser?.fullName,
                        it[1]?.currentUser?.id
                ])
            }
        }

        // 1. Searching Logic: Filter records based on the search term
        if (params.sSearch) {
            String search = params.sSearch.toLowerCase()
            records = records.findAll { row ->
                row.any { cell ->
                    cell?.toString()?.toLowerCase()?.contains(search)
                }
            }
        }

        // 2. Sorting Logic: Sort by multiple columns with stable sorting
        int sortingCols = params.iSortingCols as int
        if (sortingCols > 0) {
            // Collect sorting rules
            def sortRules = []
            (0..<sortingCols).each { idx ->
                int sortColumnIndex = params["iSortCol_${idx}"] as int
                String sortDirection = params["sSortDir_${idx}"]?.equalsIgnoreCase('asc') ? 'asc' : 'desc'
                sortRules << [column: sortColumnIndex, direction: sortDirection]
            }

            // Perform composite sort (sort by multiple columns)
            records.sort { a, b ->
                def result = 0
                for (rule in sortRules) {
                    def valueA = a[rule.column]?.toString()?.toLowerCase()
                    def valueB = b[rule.column]?.toString()?.toLowerCase()

                    result = valueA <=> valueB
                    if (rule.direction == 'desc') {
                        result = -result
                    }

                    // If there's a difference, break out and return the result
                    if (result != 0) break
                }
                return result
            }
        }

        // 3. Prepare data to render with pagination
        def dataToRender = [:]
        dataToRender.sEcho = params.sEcho
        dataToRender.aaData = []

        int displayLength = params.iDisplayLength as int
        int displayStart = params.iDisplayStart as int

        dataToRender.aaData = records.drop(displayStart).take(displayLength)

        // 4. Set total records (before and after filtering)
        dataToRender.iTotalRecords = records.size()
        dataToRender.iTotalDisplayRecords = records.size()

        // Return the final data structure for DataTables
        return dataToRender as JSON
    }





}