package com.tekdays

import grails.converters.JSON
import grails.plugin.mail.MailService
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional



@Transactional(readOnly = true)
class TekEventController {
    private static final Logger logger = LoggerFactory.getLogger(TekEventController)

    def taskService
    MailService mailService
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond TekEvent.list(params), model: [tekEventInstanceCount: TekEvent.count()]
    }

    def show(long id) {
        def tekEventInstance
        if (params.nickname) {
            tekEventInstance = TekEvent.findByNickname(params.nickname)
        } else {
            tekEventInstance = TekEvent.get(id)
        }
        if (!tekEventInstance) {
            if (params.nickname) {
                flash.message = "TekEvent not found with nickname ${params.nickname}"
            } else {
                flash.message = "TekEvent not found with id $id"
            }
            redirect(action: "list")
            return
        }
        [tekEventInstance: tekEventInstance]
    }

    def create() {
        logger.info("new event is being created")
        respond new TekEvent(params)

    }

    @Transactional
    def save(TekEvent tekEventInstance) {


        tekEventInstance.organizer = session.user
        if (tekEventInstance == null) {
            notFound()
            return
        }

       if(tekEventInstance.save (flush: true)) {
           taskService.addDefaultTask(tekEventInstance)
           mailService.sendMail{
               to tekEventInstance?.organizer?.email
               subject "New Event"
               body "You have successfully created ${tekEventInstance.name} event"

           }
       }



        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message',
                        args: [message(code: 'tekEventInstance.label',
                                default: 'TekEvent'), tekEventInstance.id])
                redirect tekEventInstance
            }
            '*' { respond tekEventInstance, [status: CREATED] }
        }
    }

    def edit(TekEvent tekEventInstance) {
        respond tekEventInstance
    }

    @Transactional
    def update(TekEvent tekEventInstance) {
        if (tekEventInstance == null) {
            notFound()
            return
        }

        if (tekEventInstance.hasErrors()) {
            respond tekEventInstance.errors, view: 'edit'
            return
        }

        tekEventInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message',
                        args: [message(code: 'TekEvent.label', default: 'TekEvent'), tekEventInstance.id])
                redirect tekEventInstance
            }
            '*' { respond tekEventInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(TekEvent tekEventInstance) {

        if (tekEventInstance == null) {
            notFound()
            return
        }

        tekEventInstance.delete flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message',
                        args: [message(code: 'TekEvent.label', default: 'TekEvent'), tekEventInstance.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message',
                        args: [message(code: 'tekEvent.label', default: 'TekEvent'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }

    @Transactional
    def volunteer() {
        def event = TekEvent.get(params.id)
        event.addToVolunteers(session.user)
        event.save()
        response.setStatus(200)
        def data = [:]
        render "Thank you for volunteering"
    }
}
