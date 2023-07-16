import { useState, useEffect } from "react";
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper } from "@mui/material";
import { visuallyHidden } from "@mui/utils";
import TableSortLabel from "@mui/material/TableSortLabel";
import { weaponImage } from './weaponImage';
import Pagination from '@mui/material/Pagination';
import { MaterialReactTable } from 'material-react-table';

type DataItem = {
    usernames: string;
    mapname: string;
    matches: number;
    wins: number;
    loss: number;
    winlossratio: number;
    kills: number;
    deaths: number;
    kdr: number;
    assists: number;
    headshots: number;
    headshot_percentage: number;
    first_weapon: string;
    second_weapon: string | null;
    hltv_rating: number;
    rws: number;
    adr: number;
    kpr: number;
    dpr: number;
    kast: number;
    mvp: number;
    _1k: number;
    _2k: number;
    _3k: number;
    _4k: number;
    _5k: number;
    _1v1: number;
    _1v2: number;
    _1v3: number;
    _1v4: number;
    _1v5: number;
    bd: number;
    bp: number;
    ff: number;
    ek: number;
    td: number;
    tda: number;
    tdh: number;
    fa: number;
    ffd: number;
    rounds: number;
    hr: number;
    ud: number;
    ebt: number;
    fbt: number;
    tk: number;
    averagewinscore: number;
    steamid: string;
  };

  const columnNames: (keyof DataItem)[] = [
    'usernames',
    'kills',
    'deaths',
    'kdr',
    'headshots',
    //'headshot_percentage',
    //'assists',
    'hltv_rating',
    //'first_weapon',
    //'second_weapon',
    'kast',
    'adr',
    'rws',
    'dpr',
    'kpr',
    'mvp',
    'ek',
    'tk',
    '_1k',
    '_2k',
    '_3k',
    '_4k',
    '_5k',
    '_1v1',
    '_1v2',
    '_1v3',
    '_1v4',
    '_1v5',
    'ff',
    'ffd',
    'bd',
    'bp',
    'hr',
    'ud',
    'ebt',
    'fbt',
    'td',
    'tda',
    'tdh',
    'fa',
    'rounds'
  ];

function descendingComparator(a: DataItem, b: DataItem, orderBy: keyof DataItem) {
    if (b[orderBy] === null || b[orderBy] === undefined) {
      return -1;
    }
    if (a[orderBy] === null || a[orderBy] === undefined) {
      return 1;
    }
  
    const aValue = a[orderBy] as number | string;
    const bValue = b[orderBy] as number | string;
  
    if (bValue < aValue) {
      return -1;
    }
    if (bValue > aValue) {
      return 1;
    }
    return 0;
  }

  function getComparator(order: "asc" | "desc", orderBy: keyof DataItem) {
    return order === 'desc'
      ? (a: DataItem, b: DataItem) => descendingComparator(a, b, orderBy)
      : (a: DataItem, b: DataItem) => -descendingComparator(a, b, orderBy);
  }
  
  function sortedRows(array: DataItem[], comparator: (a: DataItem, b: DataItem) => number) {
    const stabilizedThis = array.map((el, index) => [el, index] as [DataItem, number]);
    stabilizedThis.sort((a, b) => {
      const order = comparator(a[0], b[0]);
      if (order !== 0) return order;
      return a[1] - b[1];
    });
    return stabilizedThis.map((el) => el[0]);
  }

  function fetchWithTimeout(url: string, options: RequestInit = {}, timeout: number = 50000): Promise<Response> {
    return new Promise((resolve, reject) => {
      fetch(url, options).then(resolve, reject);
      setTimeout(reject, timeout, new Error('Timeout'));
    });
  }
  
  
  const StatsContent = () => {
    const [order, setOrder] = useState<'asc' | 'desc'>('desc');
    const [orderBy, setOrderBy] = useState<keyof DataItem>('hltv_rating');
    const [data, setData] = useState<DataItem[]>([]); // State variable for fetched data
    const [page, setPage] = useState(1);
    const [rowsPerPage, setRowsPerPage] = useState(10);
  
    useEffect(() => {
        fetchWithTimeout("https://marco.selfip.net/ixigoproxy/ixigo-dem-manager/demmanager/charts/view/PLAYER_OVERALL_STATS_EXTENDED")
          .then(response => response.text())
          .then(rawData => {
            const parsedData: DataItem[] = JSON.parse(rawData).view_data;
            setData(parsedData);
          })
          .catch(error => console.error('Error:', error));
      }, []);

    const createSortHandler = (property: keyof DataItem) => (event: React.MouseEvent<unknown>) => {
      const isAsc = orderBy === property && order === 'asc';
      setOrder(isAsc ? 'desc' : 'asc');
      setOrderBy(property);
    };
  
    return (
        <TableContainer component={Paper}>
          <Table>
            <TableHead>
              <TableRow>
                {columnNames.map((key) => (
                  <TableCell key={key}>
                    <TableSortLabel
                      active={orderBy === key}
                      direction={orderBy === key ? order : 'asc'}
                      onClick={createSortHandler(key)}
                    >
                      {key}
                      {orderBy === key ? (
                        <span style={{ clip: 'rect(0 0 0 0)', height: '1px', overflow: 'hidden', position: 'absolute', whiteSpace: 'nowrap', width: '1px' }}>
                          {order === 'desc' ? 'sorted descending' : 'sorted ascending'}
                        </span>
                      ) : null}
                    </TableSortLabel>
                  </TableCell>
                ))}
              </TableRow>
            </TableHead>
            <TableBody>
            {sortedRows(data, getComparator(order, orderBy))
                .slice((page - 1) * rowsPerPage, page * rowsPerPage)
                .map((row) => (
                <TableRow key={row.usernames}>
                  {columnNames.map((columnName) => {
                    const cellContent = row[columnName];
                    if ((columnName === 'first_weapon' || columnName === 'second_weapon') && cellContent !== null) {
                        if (typeof cellContent === 'string' && weaponImage.hasOwnProperty(cellContent)) {
                        const WeaponImage = weaponImage[cellContent as keyof typeof weaponImage];
                        return (
                            <TableCell key={columnName}>
                            <img 
                                src={WeaponImage} 
                                alt={cellContent}
                                style={{width: '60px', height: 'auto'}} // adjust as needed
                            />
                            </TableCell>
                        );
                        }
                    } else if (columnName === 'hltv_rating' && cellContent!= null && typeof cellContent === 'number') {
                        return (
                            <TableCell key={columnName} style={{ color: cellContent >= 1 ? 'green' : 'red' }}>
                                {cellContent}
                            </TableCell>
                        );
                    } 
                    return (
                        <TableCell key={columnName}>{cellContent}</TableCell>
                    );
                  })}
                </TableRow>
              ))}
            </TableBody>
          </Table>
          <Pagination count={Math.ceil(data.length / rowsPerPage)} page={page} onChange={(event, value) => setPage(value)} />
        </TableContainer>
      );
  };

  export default StatsContent;