package rs.ac.uns.ftn.BookingBaboon.services.reports.interfaces;

import rs.ac.uns.ftn.BookingBaboon.domain.reports.GuestReport;

import java.util.Collection;
import java.util.Set;

public interface IGuestReportService {
    Collection<GuestReport> getAll();
    GuestReport get(Long guestReportId);

    GuestReport create(GuestReport guestReport);

    GuestReport update(GuestReport guestReport);

    GuestReport remove(Long guestReportId);
    void removeAllForGuest(Long guestid);

    void removeAllByUser(Long userId);

    Boolean doesReportAlreadyExist(Long hostId, Long reporteeId);
}
