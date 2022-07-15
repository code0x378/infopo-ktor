-- ================================================================
-- ELECTRONICS
-- ================================================================

-- drop table electronics;

create table electronics
(
    id                      uuid not null,
    ref                     varchar(255),
    name                    varchar(255),
    loc                     varchar(255),
    notes                   TEXT,
    created_at              timestamp,
    created_by              varchar(50),
    updated_at              timestamp,
    updated_by              varchar(50),
    version                 int8,

    serial_number           varchar(255),
    model_number            varchar(255),
    status                  int,
    quantity                int,
    current_value           float,

    sold_by                 varchar(255),
    sold_at                 timestamp,
    sold_price              float,
    sold_shipping_price     float,

    manufactured_by         varchar(255),
    manufactured_at         timestamp,
    manufactured_country    varchar(255),

    obtained_from           varchar(255),
    obtained_at             timestamp,
    obtained_price          float,
    obtained_shipping_price float,

    primary key (id)
);
create index idx_electronics_ref on electronics (ref);
create index idx_electronics_name on electronics (name);
create index idx_electronics_modelnumber on electronics (model_number);
