create table rides(
      ride_id bigserial primary key,
      customer_id bigint not null references users(user_id),
      shift_id bigint references shifts(shift_id),
      promocode_id bigint references promocodes(promocode_id),

      accepted_rate_id bigint not null references rates(rate_id),

      ride_tip numeric,

      ride_distance_expected_meters numeric not null,
      ride_distance_actual_meters numeric,

      start_point geometry(Point, 4326) not null,
      end_point geometry(Point, 4326) not null,

      created_at timestamp with time zone not null default now(),
      ride_accepted_at timestamp with time zone,
      ride_driver_waiting timestamp with time zone,
      ride_starttime timestamp with time zone,
      ride_endtime timestamp with time zone,
      status ride_status,
      ride_actual_price numeric,
      constraint ride_tip_non_negative check (ride_tip > 0.0),
      constraint non_negative_distance_expected check (ride_distance_expected_meters >= 0.0),
      constraint non_negative_distance_actual check (ride_distance_actual_meters >= 0.0),
      constraint non_negative_price check (ride_actual_price >= 0.0)
);