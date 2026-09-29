package com.example.MonolitoCoche.controller;

import com.example.MonolitoCoche.model.Coche;
import com.example.MonolitoCoche.model.enums.Combustible;
import com.example.MonolitoCoche.model.enums.Transmision;
import com.example.MonolitoCoche.repository.FiltroCoche;
import com.example.MonolitoCoche.repository.Pagina;
import com.example.MonolitoCoche.service.CocheService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/coches")
public class CocheController {

    private static final String VISTA_LISTA = "coches/lista";
    private static final String VISTA_FORM = "coches/formulario";
    private static final String VISTA_DETALLE = "coches/detalle";
    private static final String REDIRECT_LISTA = "redirect:/coches";
    private static final int COCHES_POR_PAGINA = 5;

    private final CocheService cocheService;

    public CocheController(CocheService cocheService) {
        this.cocheService = cocheService;
    }

    /** Datos disponibles en todas las vistas de este controlador (opciones de los <select>). */
    @ModelAttribute
    public void datosComunes(Model model) {
        model.addAttribute("combustibles", Combustible.values());
        model.addAttribute("transmisiones", Transmision.values());
    }

    // ---------- READ ----------

    @GetMapping
    public String listar(@RequestParam(defaultValue = "0") int pagina,
                          @RequestParam(name = "combustible", required = false) List<String> combustibles,
                          @RequestParam(name = "transmision", required = false) List<String> transmisiones,
                          @RequestParam(name = "marca", required = false) List<String> marcas,
                          @RequestParam(defaultValue = "marca:asc") String orden,
                          Model model) {
        String[] ordenPartes = orden.split(":", 2);

        FiltroCoche filtro = new FiltroCoche();
        filtro.setCombustibles(combustibles == null ? List.of() : combustibles);
        filtro.setTransmisiones(transmisiones == null ? List.of() : transmisiones);
        filtro.setMarcas(marcas == null ? List.of() : marcas);
        filtro.setOrdenarPor(ordenPartes[0]);
        filtro.setDireccion(ordenPartes.length > 1 ? ordenPartes[1] : "asc");
        filtro.setPagina(Math.max(pagina, 0));
        filtro.setTamanioPagina(COCHES_POR_PAGINA);

        Pagina<Coche> resultado = cocheService.buscar(filtro);

        model.addAttribute("coches", resultado.getContenido());
        model.addAttribute("pagina", resultado);
        model.addAttribute("filtro", filtro);
        model.addAttribute("orden", orden);
        model.addAttribute("marcasDisponibles", cocheService.listarMarcas());
        return VISTA_LISTA;
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        model.addAttribute("coche", cocheService.buscarPorId(id));
        return VISTA_DETALLE;
    }

    // ---------- CREATE ----------

    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {
        model.addAttribute("coche", new Coche());
        return VISTA_FORM;
    }

    @PostMapping
    public String crear(@Valid @ModelAttribute("coche") Coche coche,
                        BindingResult result,
                        RedirectAttributes flash) {
        validarMatriculaUnica(coche, null, result);
        if (result.hasErrors()) {
            return VISTA_FORM;
        }
        Coche guardado = cocheService.guardar(coche);
        flash.addFlashAttribute("exito", "Coche " + guardado.getMatricula() + " creado correctamente.");
        return REDIRECT_LISTA;
    }

    // ---------- UPDATE ----------

    @GetMapping("/{id}/editar")
    public String formularioEditar(@PathVariable Long id, Model model) {
        model.addAttribute("coche", cocheService.buscarPorId(id));
        return VISTA_FORM;
    }

    @PostMapping("/{id}")
    public String actualizar(@PathVariable Long id,
                             @Valid @ModelAttribute("coche") Coche coche,
                             BindingResult result,
                             RedirectAttributes flash) {
        coche.setId(id); // para que la vista siga en modo edición si hay errores
        validarMatriculaUnica(coche, id, result);
        if (result.hasErrors()) {
            return VISTA_FORM;
        }
        Coche actualizado = cocheService.actualizar(id, coche);
        flash.addFlashAttribute("exito", "Coche " + actualizado.getMatricula() + " actualizado correctamente.");
        return REDIRECT_LISTA;
    }

    // ---------- DELETE ----------

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
        cocheService.eliminar(id);
        flash.addFlashAttribute("exito", "Coche eliminado correctamente.");
        return REDIRECT_LISTA;
    }

    // ---------- Auxiliares ----------

    private void validarMatriculaUnica(Coche coche, Long id, BindingResult result) {
        if (cocheService.existeMatricula(coche.getMatricula(), id)) {
            result.rejectValue("matricula", "duplicada", "Ya existe un coche con esa matrícula");
        }
    }
}
