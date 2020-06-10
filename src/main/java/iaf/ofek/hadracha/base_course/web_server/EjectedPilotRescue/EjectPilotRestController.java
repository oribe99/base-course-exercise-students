package iaf.ofek.hadracha.base_course.web_server.EjectedPilotRescue;

import iaf.ofek.hadracha.base_course.web_server.Data.CrudDataBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ejectedPilotRescue")
public class EjectPilotRestController {

    private CrudDataBase dataBase;
    private AirplanesAllocationManager manager;

    @Autowired
    public EjectPilotRestController(CrudDataBase dataBase, AirplanesAllocationManager manager) {
        this.dataBase = dataBase;
        this.manager = manager;

    }
    @GetMapping("/infos")
    public List<EjectedPilotInfo> getEjectedPilotInfos() {
        return dataBase.getAllOfType(EjectedPilotInfo.class);
    }

    @GetMapping("/takeResponsibility")
    public void takeResponsibility(@RequestParam int ejectionId, @CookieValue(value = "client-id", defaultValue = "") String clientId) {
        EjectedPilotInfo info = dataBase.getByID(ejectionId, EjectedPilotInfo.class);
        if(info.getRescuedBy()==null) {
            info.setRescuedBy(clientId);
            dataBase.update(info);
            this.manager.allocateAirplanesForEjection(info, clientId);
        }
    }

}
