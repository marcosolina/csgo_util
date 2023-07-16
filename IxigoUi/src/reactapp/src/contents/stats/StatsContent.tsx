import { useState } from "react";
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper } from "@mui/material";
import { visuallyHidden } from "@mui/utils";
import TableSortLabel from "@mui/material/TableSortLabel";
import ak47 from '../../assets/weapons/ak47.png';
import aug from '../../assets/weapons/aug.png';
import awp from '../../assets/weapons/awp.png';
import bizon from '../../assets/weapons/bizon.png';
import bomb from '../../assets/weapons/bomb.png';
import cz75a from '../../assets/weapons/cz75a.png';
import deagle from '../../assets/weapons/deagle.png';
import elite from '../../assets/weapons/elite.png';
import famas from '../../assets/weapons/famas.png';
import fiveseven from '../../assets/weapons/fiveseven.png';
import flashbang from '../../assets/weapons/flashbang.png';
import g3sg1 from '../../assets/weapons/g3sg1.png';
import galilar from '../../assets/weapons/galilar.png';
import glock from '../../assets/weapons/glock.png';
import hegrenade from '../../assets/weapons/hegrenade.png';
import hkp2000 from '../../assets/weapons/hkp2000.png';
import incgrenade from '../../assets/weapons/incgrenade.png';
import inferno from '../../assets/weapons/inferno.png';
import knife from '../../assets/weapons/knife.png';
import m249 from '../../assets/weapons/m249.png';
import m4a1 from '../../assets/weapons/m4a1.png';
import m4a1_silencer from '../../assets/weapons/m4a1_silencer.png';
import mac10 from '../../assets/weapons/mac10.png';
import mag7 from '../../assets/weapons/mag7.png';
import molotov from '../../assets/weapons/molotov.png';
import mp7 from '../../assets/weapons/mp7.png';
import mp9 from '../../assets/weapons/mp9.png';
import negev from '../../assets/weapons/negev.png';
import nova from '../../assets/weapons/nova.png';
import p250 from '../../assets/weapons/p250.png';
import p90 from '../../assets/weapons/p90.png';
import revolver from '../../assets/weapons/revolver.png';
import sawedoff from '../../assets/weapons/sawedoff.png';
import scar20 from '../../assets/weapons/scar20.png';
import sg556 from '../../assets/weapons/sg556.png';
import smokegrenade from '../../assets/weapons/smokegrenade.png';
import ssg08 from '../../assets/weapons/ssg08.png';
import taser from '../../assets/weapons/taser.png';
import tec9 from '../../assets/weapons/tec9.png';
import ump45 from '../../assets/weapons/ump45.png';
import usp_silencer from '../../assets/weapons/usp_silencer.png';
import xm1014 from '../../assets/weapons/xm1014.png';

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
    'mapname',
    'kills',
    'deaths',
    'kdr',
    'headshots',
    //'headshot_percentage',
    //'assists',
    'hltv_rating',
    'first_weapon',
    'second_weapon',
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
    'kast',
    'adr',
    'rws',
    'dpr',
    'kpr',
    'mvp',
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
  
  
  const weaponImages = {
    'ak47': ak47,
    'aug': aug,
    'awp': awp,
    'bizon': bizon,
    'bomb': bomb,
    'cz75a': cz75a,
    'deagle': deagle,
    'elite': elite,
    'famas': famas,
    'fiveseven': fiveseven,
    'flashbang': flashbang,
    'g3sg1': g3sg1,
    'galilar': galilar,
    'glock': glock,
    'hegrenade': hegrenade,
    'hkp2000': hkp2000,
    'incgrenade': incgrenade,
    'inferno': inferno,
    'knife': knife,
    'm249': m249,
    'm4a1': m4a1,
    'm4a1_silencer': m4a1_silencer,
    'mac10': mac10,
    'mag7': mag7,
    'molotov': molotov,
    'mp7': mp7,
    'mp9': mp9,
    'negev': negev,
    'nova': nova,
    'p250': p250,
    'p90': p90,
    'revolver': revolver,
    'sawedoff': sawedoff,
    'scar20': scar20,
    'sg556': sg556,
    'smokegrenade': smokegrenade,
    'ssg08': ssg08,
    'taser': taser,
    'tec9': tec9,
    'ump45': ump45,
    'usp_silencer': usp_silencer,
    'xm1014': xm1014
  };
  

const rawData: string = '[{"kills":5,"ff":0,"hltv_rating":1.35,"bd":0,"_1v3":0,"_1v2":0,"kast":0.71,"_1v1":0,"first_weapon":"fiveseven","hr":0,"mapname":"de_bank","bp":0,"ud":0.00,"rws":26.07,"loss":0,"second_weapon":"scar20","headshots":3,"assists":0,"_4k":0,"_2k":2,"usernames":"EIK3000","_1v5":0,"_1v4":0,"headshot_percentage":60.00,"deaths":2,"wins":1,"averagewinscore":3.00,"winlossratio":0,"ffd":0,"ek":1,"mvp":1,"dpr":0.29,"kpr":0.71,"matches":1,"adr":127.00,"steamid":"76561197961466528","td":0,"tda":28,"_5k":0,"_3k":0,"ebt":0.00,"tk":1,"kdr":2.50,"_1k":1,"tdh":889,"fbt":0.00,"fa":0,"rounds":7},{"kills":5,"ff":0,"hltv_rating":0.43,"bd":0,"_1v3":0,"_1v2":0,"kast":0.38,"_1v1":0,"first_weapon":"p90","hr":0,"mapname":"de_bank","bp":0,"ud":0.00,"rws":21.23,"loss":1,"second_weapon":"g3sg1","headshots":2,"assists":1,"_4k":0,"_2k":1,"usernames":"Handbagatha Christie","_1v5":0,"_1v4":0,"headshot_percentage":40.00,"deaths":12,"wins":0,"averagewinscore":-3.00,"winlossratio":0.00,"ffd":22,"ek":4,"mvp":2,"dpr":0.92,"kpr":0.38,"matches":1,"adr":72.38,"steamid":"76561197963053225","td":2,"tda":158,"_5k":0,"_3k":0,"ebt":9.87,"tk":0,"kdr":0.42,"_1k":3,"tdh":941,"fbt":2.90,"fa":1,"rounds":13},{"kills":18,"ff":0,"hltv_rating":1.97,"bd":0,"_1v3":0,"_1v2":0,"kast":0.92,"_1v1":1,"first_weapon":"ak47","hr":0,"mapname":"de_bank","bp":0,"ud":10.00,"rws":30.32,"loss":0,"second_weapon":"m4a1_silencer","headshots":9,"assists":2,"_4k":0,"_2k":7,"usernames":"Wagatha Christie","_1v5":0,"_1v4":0,"headshot_percentage":50.00,"deaths":5,"wins":1,"averagewinscore":3.00,"winlossratio":0,"ffd":24,"ek":3,"mvp":6,"dpr":0.38,"kpr":1.38,"matches":1,"adr":194.69,"steamid":"76561197974132960","td":0,"tda":128,"_5k":0,"_3k":0,"ebt":6.73,"tk":2,"kdr":3.60,"_1k":4,"tdh":2531,"fbt":2.54,"fa":0,"rounds":13},{"kills":9,"ff":0,"hltv_rating":1.06,"bd":1,"_1v3":0,"_1v2":0,"kast":0.77,"_1v1":0,"first_weapon":"sg556","hr":0,"mapname":"de_bank","bp":0,"ud":3.00,"rws":17.18,"loss":0,"second_weapon":"inferno","headshots":3,"assists":3,"_4k":0,"_2k":1,"usernames":"S.S.G. Tolkien","_1v5":0,"_1v4":0,"headshot_percentage":33.33,"deaths":6,"wins":1,"averagewinscore":3.00,"winlossratio":0,"ffd":8,"ek":5,"mvp":1,"dpr":0.46,"kpr":0.69,"matches":1,"adr":97.85,"steamid":"76561198093594380","td":1,"tda":21,"_5k":0,"_3k":0,"ebt":4.86,"tk":0,"kdr":1.50,"_1k":7,"tdh":1272,"fbt":0.00,"fa":0,"rounds":13},{"kills":7,"ff":0,"hltv_rating":0.90,"bd":0,"_1v3":0,"_1v2":1,"kast":0.46,"_1v1":2,"first_weapon":"m4a1_silencer","hr":0,"mapname":"de_bank","bp":1,"ud":33.00,"rws":16.56,"loss":1,"second_weapon":"sg556","headshots":3,"assists":1,"_4k":0,"_2k":3,"usernames":"chiCkin","_1v5":0,"_1v4":0,"headshot_percentage":42.86,"deaths":8,"wins":0,"averagewinscore":-3.00,"winlossratio":0.00,"ffd":15,"ek":0,"mvp":3,"dpr":0.62,"kpr":0.54,"matches":1,"adr":79.38,"steamid":"76561199057392192","td":0,"tda":102,"_5k":0,"_3k":0,"ebt":13.65,"tk":1,"kdr":0.88,"_1k":1,"tdh":1032,"fbt":0.55,"fa":0,"rounds":13},{"kills":1,"ff":0,"hltv_rating":0.13,"bd":0,"_1v3":0,"_1v2":0,"kast":0.15,"_1v1":0,"first_weapon":"sg556","hr":0,"mapname":"de_bank","bp":1,"ud":0.00,"rws":0.67,"loss":1,"second_weapon":null,"headshots":0,"assists":0,"_4k":0,"_2k":0,"usernames":"Lolly11luce","_1v5":0,"_1v4":0,"headshot_percentage":0.00,"deaths":12,"wins":0,"averagewinscore":-3.00,"winlossratio":0.00,"ffd":0,"ek":0,"mvp":0,"dpr":0.92,"kpr":0.08,"matches":1,"adr":14.54,"steamid":"76561199465861599","td":1,"tda":0,"_5k":0,"_3k":0,"ebt":0.00,"tk":0,"kdr":0.08,"_1k":1,"tdh":189,"fbt":0.00,"fa":0,"rounds":13}]';

const data: DataItem[] = JSON.parse(rawData);

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
  
  const StatsContent = () => {
    const [order, setOrder] = useState<'asc' | 'desc'>('asc');
    const [orderBy, setOrderBy] = useState<keyof DataItem>('kills');
  
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
              {sortedRows(data, getComparator(order, orderBy)).map((row) => (
                <TableRow key={row.usernames}>
                  {columnNames.map((columnName) => {
                    const cellContent = row[columnName];
                    if ((columnName === 'first_weapon' || columnName === 'second_weapon') && cellContent !== null) {
                        if (typeof cellContent === 'string' && weaponImages.hasOwnProperty(cellContent)) {
                        const WeaponImage = weaponImages[cellContent as keyof typeof weaponImages];
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
                    } 
                    return (
                        <TableCell key={columnName}>{cellContent}</TableCell>
                    );
                  })}
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      );
      
      
      
      
      
      
      
  };
  
  export default StatsContent;