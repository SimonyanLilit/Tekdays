package com.tekdays

import javax.mail.Session

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class TekMessageController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def list
        def count
        def event = TekEvent.get(params.id)
        if(event){
            list = TekMessage.findAllByEvent(event, params)
            count = TekMessage.countByEvent(event)
        }
        else {
            list = TekMessage.list(params)
            count = TekMessage.count()
        }
        render view:'ajaxIndex', model:[tekMessageInstanceList: list,
                                        tekMessageInstanceCount: count,
                                        event: event]
    }
    def show(TekMessage tekMessageInstance) {
        respond tekMessageInstance
    }

    def create() {
        def tekEventInstance = TekEvent.read(params["tekEvent.id"])
        params.event = tekEventInstance
        respond new TekMessage(params)
    }

    @Transactional
    def save(TekMessage tekMessageInstance) {
        if (tekMessageInstance == null) {
            notFound()
            return
        }

        if (tekMessageInstance.hasErrors()) {
            respond tekMessageInstance.errors, view:'create'
            return
        }

        tekMessageInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'tekMessage.label', default: 'TekMessage'), tekMessageInstance.id])
                redirect tekMessageInstance
            }
            '*' { respond tekMessageInstance, [status: CREATED] }
        }
    }

    def edit(TekMessage tekMessageInstance) {
        respond tekMessageInstance
    }

    @Transactional
    def update(TekMessage tekMessageInstance) {
        if (tekMessageInstance == null) {
            notFound()
            return
        }

        if (tekMessageInstance.hasErrors()) {
            respond tekMessageInstance.errors, view:'edit'
            return
        }

        tekMessageInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'TekMessage.label', default: 'TekMessage'), tekMessageInstance.id])
                redirect tekMessageInstance
            }
            '*'{ respond tekMessageInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(TekMessage tekMessageInstance) {

        if (tekMessageInstance == null) {
            notFound()
            return
        }

        tekMessageInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'TekMessage.label', default: 'TekMessage'), tekMessageInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'tekMessage.label', default: 'TekMessage'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
    def showDetail() {
        def tekMessageInstance = TekMessage.get(params.id)
        if (tekMessageInstance) {
            render(template:"details", model:[tekMessageInstance:tekMessageInstance])
        }
        else {
            render "No message found with id: ${params.id}"
        }
    }
    def reply = {
        def parent = TekMessage.get(params.id)
        def tekMessageInstance = new TekMessage(parent:parent, event:parent.event,
                subject:"RE: $parent.subject")
        render view:'create', model:['tekMessageInstance':tekMessageInstance]
    }
}
