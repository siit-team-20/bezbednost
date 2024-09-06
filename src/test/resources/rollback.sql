
INSERT INTO public.available_period
(id, price_per_night, start_date, end_date) VALUES

                                                (15, 90, '2024-04-21', '2024-04-30') ON CONFLICT (id) DO NOTHING ;

UPDATE public.available_period
SET id=18, price_per_night=75, end_date='2024-05-31', start_date='2024-05-21'
WHERE id=18;


DELETE FROM public.accommodations_available_periods where available_periods_id > 45 and accommodation_id=9;
DELETE FROM public.available_period where id > 45 and price_per_night=8;

INSERT INTO public.accommodations_available_periods (accommodation_id,available_periods_id) VALUES (9,15) ;

UPDATE public.accommodations
SET cancellation_deadline=5
WHERE id=9;

COMMIT;