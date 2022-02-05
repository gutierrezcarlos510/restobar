
INSERT INTO public.persona
(ci_per, nom_per, priape_per, segape_per, tel_per, dir_per, ema_per, fot_per, est_per, sex_per, cod_per, codbio_per, codesc_per)
VALUES('0000000', 'S', '/', 'N', ' ', ' ', ' ', 'avatar00.jpg', true, true, 0, '0000', '0-20160101');
INSERT INTO public.persona
(ci_per, nom_per, priape_per, segape_per, tel_per, dir_per, ema_per, fot_per, est_per, sex_per, cod_per, codbio_per, codesc_per)
VALUES('7167968', 'Carlos Franz', 'Gutierrez', 'Gutierrez', '75136609', 'Calle Mexico entre German', '', 'avatar00.jpg', true, true, 1, '', '1-99-20160101');


INSERT INTO public.dato
(log_dat, cla_dat, fecha_alta, fecha_baja, cod_per)
VALUES('admin', 'admin7167968', '2021-04-14 10:08:24.346', NULL, 1);

INSERT INTO public.usurol
(cod_rol, fecha_alta, fecha_baja, cod_per)
VALUES(1, '2016-04-11 07:49:51.000', NULL, 1);
INSERT INTO public.usurol
(cod_rol, fecha_alta, fecha_baja, cod_per)
VALUES(2, '2016-04-27 02:42:48.000', NULL, 1);

INSERT INTO public.docente
(cod_doc, pro_doc, abrpro_doc, est_doc)
VALUES(0, 'S/N', '-', true);

INSERT INTO public.cliente
(cod_cli, est_cli)
VALUES(0, true);


delete from ins