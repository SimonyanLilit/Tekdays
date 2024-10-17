class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(view:"/index")
        "500"(view:'/error')
        "/events/$nickname"{
            controller = "tekEvent"
            action = "show"
        }
        "/TakDays/tekEvent/index$lang"(controller: 'tekEvent')
        "/TakDays/tekEvent/create$lang"(controller: 'tekEvent')
	}
}
