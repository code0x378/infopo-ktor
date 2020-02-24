-- ================================================================
-- APPAREL
-- ================================================================

-- drop table apparel

create table apparel (
    id uuid not null,
    ref varchar(255),
    name varchar(255),
    active boolean not null default true,
    loc varchar(255),
    size_xxs int,
    size_xs int,
    size_s int,
    size_m int,
    size_l int,
    size_xl int,
    notes TEXT,
    created_at timestamp,
    created_by varchar(50),
    updated_at timestamp,
    updated_by varchar(50),
    version int8,
    primary key (id)
);
create index idx_apparel_ref on apparel (ref);
create index idx_apparel_name on apparel (name);
