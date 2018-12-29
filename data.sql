-- addresses
INSERT INTO addresses (id, street, city, zipCode)
VALUES (100, 'Old Gate Drive', 'Prague', '120 00');
INSERT INTO addresses (id, street, city, zipCode)
VALUES (101, 'Saint Paul Crossing', 'Los Angeles', '3313');

-- company

INSERT INTO companies (id, name, ico, description, address_id)
VALUES (1, 'Google', '53590216', 'We dont have to introduce ourselfs', 100);
INSERT INTO companies (id, name, ico, description, address_id)
VALUES (2, 'Sodexo', '75682767', 'We find what you are looking for', 101);

-- categories 

INSERT INTO categories (id, name) VALUES (1, 'Business Development');
INSERT INTO categories (id, name) VALUES (2, 'Legal');
INSERT INTO categories (id, name) VALUES (3, 'Human Resources');
INSERT INTO categories (id, name) VALUES (4, 'Marketing');

-- admin

INSERT INTO admins (id, firstName, lastName, username, password, role)
VALUES (42, 'Ross', 'Smith', 'smith@brigade.com', '$2a$10$ycJlGsmkoKMizaHqlFUiL.sNH1.rEq1hbSTllj9865S',
        'ADMIN');


-- workers
INSERT INTO workers (id, firstName, lastName, username, password, role)
VALUES (100, 'Hally', 'Stain', 'hstain@cisco.com', '$2a$10$ycJlGsmkoKMizaHqlFUiL.sNH1.rEq1hbSTllje73RyYVNR55tHWS',
        'WORKER');
INSERT INTO workers (id, firstName, lastName, username, password, role)
VALUES (102, 'Karine', 'Seer', 'kseer1@spotify.com', '154de7167f65ee702a0784cf340a389e5613aa5ed029ad514fe8bc3ba392fc67',
        'WORKER');
INSERT INTO workers (id, firstName, lastName, username, password, role)
VALUES (103, 'Dione', 'Dotson', 'ddotson2@goo.gl', 'f1fbf392dc249bf53a211a74891aed0fe375829825f5e8356d8120b0bba64b8d',
        'WORKER');
INSERT INTO workers (id, firstName, lastName, username, password, role)
VALUES (104, 'Roldan', 'Siburn', 'rsiburn3@utexas.edu',
        '46b6be12a1b6ca0389b4eca1d7da28be3903606757770aee4fc5f4d607246034',
        'WORKER');
INSERT INTO workers (id, firstName, lastName, username, password, role)
VALUES (105, 'Obie', 'Danat', 'odanat4@shinystat.com',
        '2efe86919a6f63252ca89a7be4b8e9675ac5f581eae1bf8131152da6688e0c18',
        'WORKER');
INSERT INTO workers (id, firstName, lastName, username, password, role)
VALUES (106, 'Georgianne', 'Le Conte', 'gleconte5@hostgator.com',
        'ebde75f21ee04efeb25f05e2f55148beba76ab924f0387796a1c818e05e7070b', 'WORKER');
INSERT INTO workers (id, firstName, lastName, username, password, role)
VALUES (107, 'Gerta', 'Forbear', 'gforbear6@furl.net',
        'e5cd73170b77e5828141368966967f2f926951cdb5620c323258219e860dc786',
        'WORKER');
INSERT INTO workers (id, firstName, lastName, username, password, role)
VALUES (108, 'Algernon', 'Tolotti', 'atolotti7@uol.com.br',
        '3a78df91aa91f34542153ac2d2f64b6482bf4998b12028f42ed1b95133923b1a',
        'WORKER');
INSERT INTO workers (id, firstName, lastName, username, password, role)
VALUES (109, 'Brent', 'Helis', 'bhelis8@reddit.com', '42985d2a2255eba1d226613d5b72493eaaff52c35369541a51d866de2bbe43fc',
        'WORKER');
INSERT INTO workers (id, firstName, lastName, username, password, role)
VALUES (110, 'Camella', 'Haythorn', 'chaythorn9@bloomberg.com',
        '401671484d60a592b69e8dde60aa4e717019b5602707afc8455214175dbcf137', 'WORKER');

-- employers

INSERT INTO employers (id, firstName, lastName, username, password, role, company_id)
VALUES (1, 'John', 'Doe', 'lost@gmail.com', '$2a$10$ycJlGsmkoKMizaHqlFUiL.sNH1.rEq1hbSTllje73RyYVNR55tHWS',
        'EMPLOYER', 1);
INSERT INTO employers (id, firstName, lastName, username, password, role, company_id)
VALUES
  (2, 'Peter', 'Long', 'peter@sodexo.cz', '401671484d60a592b69e8dde60aa4e717019b5602707afc8455214175dbcf137',
   'EMPLOYER', 2);

-- brigades

INSERT INTO brigades (id, salaryPerHour, dateFrom, dateTo, description, position, timeFrom, name, duration, maxWorkers,
                      thumbsUp, thumbsDown, category_id)
VALUES (100, 224, '10/06/2019', '10/7/2019',
        'In hac habitasse platea dictumst. Etiam faucibus cursus urna. Ut tellus. Nulla ut erat id mauris vulputate elementum. Nullam varius.',
        'Services', '18:51', 'night shift', 5, 18, 11, 3, 1);
INSERT INTO brigades (id, salaryPerHour, dateFrom, dateTo, description, position, timeFrom, name, duration, maxWorkers,
                      thumbsUp, thumbsDown, category_id)
VALUES (1002, 138, '12/18/2019', '12/27/2019',
        'Integer tincidunt ante vel ipsum. Praesent blandit lacinia erat. Morbi porttitor lorem id ligula.',
        'Services', '5:24', 'java developer', 12, 16, 1, 5, 2);
INSERT INTO brigades (id, salaryPerHour, dateFrom, dateTo, description, position, timeFrom, name, duration, maxWorkers,
                      thumbsUp, thumbsDown, category_id)
VALUES (103, 204, '09/17/2018', '08/02/2018', 'Sed sagittis. Praesent id massa id nisl venenatis lacinia.', 'Services',
        '8:16',
        '15:39', 10, 9, 14, 15, 3);
INSERT INTO brigades (id, salaryPerHour, dateFrom, dateTo, description, position, timeFrom, name, duration, maxWorkers,
                      thumbsUp, thumbsDown, category_id)
VALUES (104, 212, '11/04/2018', '10/01/2018', 'Suspendisse potenti.', 'Services', '1:49', 'python', 9, 12, 2, 8, 4);
INSERT INTO brigades (id, salaryPerHour, dateFrom, dateTo, description, position, timeFrom, name, duration, maxWorkers,
                      thumbsUp, thumbsDown, category_id)
VALUES (105, 216, '12/03/2018', '11/15/2018',
        'Integer a nibh. Suspendisse potenti. Nullam porttitor lacus at turpis. Donec posuere metus vitae ipsum. Aliquam non mauris. Morbi non lectus.',
        'Accounting', '21:02', 'foundation', 7, 2, 3, 9, 1);
INSERT INTO brigades (id, salaryPerHour, dateFrom, dateTo, description, position, timeFrom, name, duration, maxWorkers,
                      thumbsUp, thumbsDown, category_id)
VALUES (107, 99, '03/05/2018', '12/13/2017',
        'In eleifend quam a odio. In hac habitasse platea dictumst. Maecenas ut massa quis augue luctus tincidunt.',
        'Sales', '13:04', 'tree plant', 11, 18, 12, 2, 2);
INSERT INTO brigades (id, salaryPerHour, dateFrom, dateTo, description, position, timeFrom, name, duration, maxWorkers,
                      thumbsUp, thumbsDown, category_id)
VALUES (108, 178, '11/07/2018', '06/18/2018', 'Vestibulum rutrum rutrum neque. In blandit ultrices enim.',
        'Business Development', '18:45', 'recepce', 9, 13, 6, 5, 4);
INSERT INTO brigades (id, salaryPerHour, dateFrom, dateTo, description, position, timeFrom, name, duration, maxWorkers,
                      thumbsUp, thumbsDown, category_id)
VALUES (109, 208, '08/25/2018', '09/02/2018', 'Etiam faucibus cursus urna. Ut tellus. Donec semper sapien a libero.',
        'Legal',
        '4:52', '18:04', 9, 6, 15, 8, 3);
INSERT INTO brigades (id, salaryPerHour, dateFrom, dateTo, description, position, timeFrom, name, duration, maxWorkers,
                      thumbsUp, thumbsDown, category_id)
VALUES (110, 228, '09/12/2018', '02/02/2018',
        'Mauris lacinia sapien quis libero. Morbi quis tortor id nulla ultrices aliquet. Maecenas leo odio, condimentum id, luctus nec, molestie sed, justo.',
        'Product Management', '7:35', 'vytvor bakalarku', 9, 2, 3, 5, 1);
INSERT INTO brigades (id, salaryPerHour, dateFrom, dateTo, description, position, timeFrom, name, duration, maxWorkers,
                      thumbsUp, thumbsDown, category_id)
VALUES (111, 118, '03/18/2018', '09/24/2018',
        'Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Pellentesque eget nunc. Maecenas pulvinar lobortis est.',
        'Human Resources', '5:17', 'save barry', 12, 8, 12, 9, 1);
INSERT INTO brigades (id, salaryPerHour, dateFrom, dateTo, description, position, timeFrom, name, duration, maxWorkers,
                      thumbsUp, thumbsDown, category_id)
VALUES (112, 187, '12/12/2018', '04/03/2018',
        'Quisque porta volutpat erat. Proin leo odio, porttitor id, consequat in, consequat ut, nulla. Sed accumsan felis.',
        'Legal', '0:23', 'kill voldemort', 4, 12, 13, 13, 4);
INSERT INTO brigades (id, salaryPerHour, dateFrom, dateTo, description, position, timeFrom, name, duration, maxWorkers,
                      thumbsUp, thumbsDown, category_id)
VALUES (119, 110, '02/21/2018', '11/09/2018', 'Duis aliquam convallis nunc. Proin at turpis a pede posuere nonummy.',
        'Sales',
        '23:40', 'supr uklid', 6, 9, 13, 4, 1);
INSERT INTO brigades (id, salaryPerHour, dateFrom, dateTo, description, position, timeFrom, name, duration, maxWorkers,
                      thumbsUp, thumbsDown, category_id)
VALUES (121, 125, '06/03/2018', '09/17/2018', 'Duis mattis egestas metus. Aenean fermentum.', 'Business Development',
        '3:08',
        'name brigade', 4, 20, 4, 10, 4);
INSERT INTO brigades (id, salaryPerHour, dateFrom, dateTo, description, position, timeFrom, name, duration, maxWorkers,
                      thumbsUp, thumbsDown, category_id)
VALUES (122, 116, '09/30/2018', '02/02/2018',
        'In hac habitasse platea dictumst. Aliquam augue quam, sollicitudin vitae, consectetuer eget, rutrum at, lorem. ',
        'Sales', '13:09', '15:15', 10, 12, 11, 13, 2);
INSERT INTO brigades (id, salaryPerHour, dateFrom, dateTo, description, position, timeFrom, name, duration, maxWorkers,
                      thumbsUp, thumbsDown, category_id)
VALUES (125, 219, '12/31/2017', '04/22/2018', 'Praesent id massa id nisl venenatis lacinia.', 'Accounting', '14:36',
        'supr brig',
        5, 7, 3, 3, 1);

INSERT INTO brigade_worker (brigades_id, workers_id)
VALUES (103, 100);

INSERT INTO brigade_worker (brigades_id, workers_id)
VALUES (103, 103);

INSERT INTO brigade_worker (brigades_id, workers_id)
VALUES (103, 104);

