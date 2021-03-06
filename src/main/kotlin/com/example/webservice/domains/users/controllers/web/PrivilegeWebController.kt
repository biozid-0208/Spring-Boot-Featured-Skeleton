package com.example.webservice.domains.users.controllers.web

import com.example.webservice.commons.utils.ExceptionUtil
import com.example.webservice.domains.users.models.dtos.PrivilegeDto
import com.example.webservice.domains.users.models.dtos.RoleDto
import com.example.webservice.domains.users.models.mappers.PrivilegeMapper
import com.example.webservice.domains.users.models.mappers.RoleMapper
import com.example.webservice.domains.users.services.PrivilegeService
import com.example.webservice.domains.users.services.RoleService
import com.example.webservice.routing.Route
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@Controller
class PrivilegeWebController @Autowired constructor(
        private val privilegeService: PrivilegeService,
        private val privilegeMapper: PrivilegeMapper
) {

    @GetMapping(Route.V1.WEB_PRIVILEGES_PAGE)
    fun rolesPage(model: Model): String {
        val privileges = this.privilegeService.findAll()

        model.addAttribute("privileges", privileges)
        return "material/fragments/roles/privileges"
    }

    @PostMapping(Route.V1.WEB_PRIVILEGE_CREATE)
    fun createRole(@Valid @ModelAttribute privilegeDto: PrivilegeDto): String {
        val privilege = this.privilegeService.save(this.privilegeMapper.map(privilegeDto, null))
        return "redirect:/admin/privileges/${privilege.id}"
    }

    @GetMapping(Route.V1.WEB_PRIVILEGE_DETAILS_PAGE)
    fun roleDetailsPage(@PathVariable("privilege_id") privilegeId: Long,
                        model: Model): String {

        val selectedPrivilege = this.privilegeService.find(privilegeId).orElseThrow { ExceptionUtil.getNotFound("Privilege", privilegeId) }

        val privileges = this.privilegeService.findAll()

        model.addAttribute("selectedPrivilege", selectedPrivilege)
        model.addAttribute("privileges", privileges)
        return "material/fragments/roles/privileges"
    }

    @PostMapping(Route.V1.WEB_PRIVILEGE_UPDATE)
    fun updateRole(@PathVariable("privilege_id") privilegeId: Long,
                   @Valid @ModelAttribute privilegeDto: PrivilegeDto): String {
        var privilege = this.privilegeService.find(privilegeId).orElseThrow { ExceptionUtil.getNotFound("Privilege", privilegeId) }
        if (privilege.name == "ADMINISTRATION") throw ExceptionUtil.forbidden("Updating privilege ADMINISTRATION is not possible.")
        privilege = this.privilegeService.save(this.privilegeMapper.map(privilegeDto, privilege))
        return "redirect:/admin/privileges/${privilege.id}"
    }

}