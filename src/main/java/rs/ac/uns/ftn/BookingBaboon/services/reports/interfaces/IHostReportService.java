package rs.ac.uns.ftn.BookingBaboon.services.reports.interfaces;

import rs.ac.uns.ftn.BookingBaboon.domain.reports.HostReport;

import java.util.Collection;
import java.util.Set;

public interface IHostReportService {
    Collection<HostReport> getAll();
    HostReport get(Long hostReportId);

    HostReport create(HostReport hostReport);

    HostReport update(HostReport hostReport);

    HostReport remove(Long hostReportId);
    void removeAllForHost(Long hostId);

    void removeAllByUser(Long userId);

    Boolean doesReportAlreadyExist(Long hostId, Long reporteeId);
}
