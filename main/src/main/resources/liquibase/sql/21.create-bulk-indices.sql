-- дописать индексы после рефакторинга !!!!!!!
create index users_role_id_idx on public.users using btree(role_id);
create index cabs_vehicle_id_idx on public.cabs using btree(vehicle_id);
create index vehicles_brand_id_idx on public.vehicles using btree(brand_id);
create index shifts_cab_id_idx on public.shifts using btree(cab_id);
create index shifts_driver_id_idx on public.shifts using btree(cab_id);
-- create index rides_shift_id_idx on public.rides using btree(ride_id);
-- create index rides_customer_id_idx on public.rides using btree(customer_id);
-- create index rides_promocode_id_idx on public.rides using btree(promocode_id);

create index phone_number_idx on public.users using hash (phone_number);
create index city_name_idx on public.cities using btree (city_name);
-- create index rides_data_idx on public.rides using btree (ride_distance_meters);
create index customer_rating_idx on public.customer_ratings using btree (rating);