package com.tekdays

import grails.converters.JSON
import grails.converters.XML

import groovy.sql.Sql

import org.hibernate.SessionFactory

import javax.persistence.EntityManager
import javax.sql.DataSource

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class TekUserController {
    def datatablesSourceService

    DataSource dataSource

    def dtList() {
        println("aaa")
        [tekUserId: params.id]
    }

    def revisionDatatables() {
        def propertiesToRender = ["fullName", "email", "website", "bio", "dateCreated", "lastUpdated","changedBy","changerId"]
        render datatablesSourceService.dataTablesSourceForAudit(propertiesToRender, params)
    }

    def revision() {
        [tekUserId: params.id]
    }


    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE", getAllUsers: "GET", getUserById: "GET", createUser: "POST"]




//REST

    def getAllUsers = {
        def tekUsers = TekUser.list()
        if (tekUsers) {
            withFormat {
                json {
                    def jsonify = tekUsers as JSON
                    jsonify.prettyPrint = true
                    render jsonify
                }
                xml {
                    render tekUsers as XML
                }
            }
        } else response.sendError 404
    }
    def getUserById(Long id) {
        TekUser tekUser = TekUser.findById(id)
        if (tekUser) {
            withFormat {
                json {
                    def jsonify = tekUser as JSON
                    jsonify.prettyPrint = true
                    render jsonify
                }
                xml {
                    render(tekUser as XML)
                }
            }
        } else response.sendError 404
    }
//    @Transactional
//    def createUser() {
//        def slurped = new JsonSlurper()
//        def requestMap = slurped.parseText(request?.JSON?.toString())
//        def fullName = requestMap["fullName"]
//        def userName = requestMap["userName"]
//        def email = requestMap["email"]
//        def website = requestMap["website"]
//        def bio = requestMap["bio"]
//        def password = requestMap["password"]
//        TekUser tekUser = new TekUser(fullName: fullName, userName: userName, email: email, website: website, bio: bio, password: password)
//        tekUser.save flush: true
//        render("NAME= ${tekUser.fullName}") as JSON
//    }

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond TekUser.list(params), model:[tekUserInstanceCount: TekUser.count()]
    }

    def show(TekUser tekUserInstance) {
        respond tekUserInstance
    }

    def create() {

        respond new TekUser(params)
    }

    @Transactional
    def save(TekUser tekUserInstance) {
        if (tekUserInstance == null) {
            notFound()
            return
        }

        if (tekUserInstance.hasErrors()) {
            respond tekUserInstance.errors, view:'create'
            return
        }

        tekUserInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'tekUser.label', default: 'TekUser'), tekUserInstance.id])
                redirect tekUserInstance
            }
            '*' { respond tekUserInstance, [status: CREATED] }
        }
    }

    def edit(TekUser tekUserInstance) {
        respond tekUserInstance
    }

    @Transactional
    def update(TekUser tekUserInstance) {
        if (tekUserInstance == null) {
            notFound()
            return
        }

        if (tekUserInstance.hasErrors()) {
            respond tekUserInstance.errors, view:'edit'
            return
        }

        tekUserInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'TekUser.label', default: 'TekUser'), tekUserInstance.id])
                redirect tekUserInstance
            }
            '*'{ respond tekUserInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(TekUser tekUserInstance) {

        if (tekUserInstance == null) {
            notFound()
            return
        }

        def tekEventByOrganizer = TekEvent.findByOrganizer(tekUserInstance)

        def sql = new Sql(dataSource)
        String query = "SELECT e.id FROM tek_event e WHERE e.id IN (SELECT eu.tek_event_volunteers_id FROM tek_event_tek_user AS eu WHERE eu.tek_user_id = ${tekUserInstance.id})"
        def tekEventByVolunteers = sql.rows(query)
        sql.close()

        if (tekEventByOrganizer || tekEventByVolunteers){
            flash.error = message(code: 'default.not.deleted.message', args: [message(code: 'TekUser.label', default: 'TekUser'), tekUserInstance.id])
            redirect action:"index", method:"GET"
        } else {
            tekUserInstance.delete flush: true
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'TekUser.label', default: 'TekUser'), tekUserInstance.id])
            redirect action:"index", method:"GET"
        }
//        request.withFormat {
//            form multipartForm {
//                flash.message = message(code: 'default.deleted.message', args: [message(code: 'TekUser.label', default: 'TekUser'), tekUserInstance.id])
//                redirect action:"index", method:"GET"
//            }
//            '*'{ render status: NO_CONTENT }
//        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'tekUser.label', default: 'TekUser'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
    def login() {
        if (params.cName)
            return [cName: params.cName, aName: params.aNames]
    }
    def validate() {
        def user = TekUser.findByUserName(params.username)
        if (user && user.password == params.password){
            session.user = user
            if (params.cName)
                redirect controller:params.cName, action:params.aName
            else

            redirect (url: '/')
        }
        else{
            flash.message = "Invalid username and password."
            render view:'login'
        }
    }
    def logout(){
        session.user=null
        redirect(url: '/')
    }
    def dataTablesRenderer() {
        def propertiesToRender = ["fullName", "email", "website", "bio", "id"]
        def entityName = 'TekUser'
        render datatablesSourceService.dataTablesSource(propertiesToRender, entityName, params)
    }
    def chatroom(){}
}

